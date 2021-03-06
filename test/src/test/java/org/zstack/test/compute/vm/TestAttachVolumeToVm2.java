package org.zstack.test.compute.vm;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.zstack.core.cloudbus.CloudBus;
import org.zstack.core.componentloader.ComponentLoader;
import org.zstack.core.db.DatabaseFacade;
import org.zstack.core.db.SimpleQuery;
import org.zstack.core.db.SimpleQuery.Op;
import org.zstack.header.configuration.DiskOfferingVO;
import org.zstack.header.configuration.DiskOfferingVO_;
import org.zstack.header.storage.primary.PrimaryStorageInventory;
import org.zstack.header.vm.VmInstanceInventory;
import org.zstack.header.volume.VolumeInventory;
import org.zstack.header.volume.VolumeStatus;
import org.zstack.header.volume.VolumeVO;
import org.zstack.test.Api;
import org.zstack.test.ApiSenderException;
import org.zstack.test.DBUtil;
import org.zstack.test.deployer.Deployer;

public class TestAttachVolumeToVm2 {
    Deployer deployer;
    Api api;
    ComponentLoader loader;
    CloudBus bus;
    DatabaseFacade dbf;

    @Before
    public void setUp() throws Exception {
        DBUtil.reDeployDB();
        deployer = new Deployer("deployerXml/vm/TestAttachVolumeToVm.xml");
        deployer.build();
        api = deployer.getApi();
        loader = deployer.getComponentLoader();
        bus = loader.getComponent(CloudBus.class);
        dbf = loader.getComponent(DatabaseFacade.class);
    }

    @Test
    public void test() throws ApiSenderException {
        PrimaryStorageInventory pinv = api.listPrimaryStorage(null).get(0);
        SimpleQuery<DiskOfferingVO> dq = dbf.createQuery(DiskOfferingVO.class);
        dq.add(DiskOfferingVO_.name, Op.EQ, "TestDataDiskOffering");
        DiskOfferingVO dvo = dq.find();
        VolumeInventory vinv = api.createDataVolume("TestData", dvo.getUuid());
        /*fake a ready volume*/
        VolumeVO vvo = dbf.findByUuid(vinv.getUuid(), VolumeVO.class);
        vvo.setStatus(VolumeStatus.Ready);
        vvo.setPrimaryStorageUuid(pinv.getUuid());
        dbf.update(vvo);
        vinv = VolumeInventory.valueOf(vvo);

        VmInstanceInventory vminv = api.listVmInstances(null).get(0);
        vinv = api.attachVolumeToVm(vminv.getUuid(), vinv.getUuid());
        Assert.assertTrue(vinv.isAttached());
        Assert.assertEquals(VolumeStatus.Ready.toString(), vinv.getStatus());
        Assert.assertNotNull(vinv.getVmInstanceUuid());
        vvo = dbf.findByUuid(vinv.getUuid(), VolumeVO.class);
        Assert.assertTrue(vvo.isAttached());
        Assert.assertEquals(VolumeStatus.Ready, vvo.getStatus());
        Assert.assertNotNull(vvo.getVmInstanceUuid());
    }
}
