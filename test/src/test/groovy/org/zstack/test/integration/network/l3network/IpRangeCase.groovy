package org.zstack.test.integration.network.l3network

import org.zstack.header.network.l3.L3NetworkCategory
import org.zstack.network.l3.IpNotAvailabilityReason
import org.zstack.sdk.*
import org.zstack.test.integration.kvm.KvmTest
import org.zstack.test.integration.network.NetworkTest
import org.zstack.testlib.*

import static java.util.Arrays.asList


/**
 * Created by shixin on 2018/05/29.
 */
class IpRangeCase extends SubCase {
    EnvSpec env

    @Override
    void clean() {
        env.delete()
    }

    @Override
    void setup() {
        useSpring(NetworkTest.springSpec)
        useSpring(KvmTest.springSpec)

    }
    @Override
    void environment() {
        env = Env.OneIpL3Network()
    }

    @Override
    void test() {
        env.create {
            testAddIpRangeToDifferentL3ButSameL2()
        }
    }

    void testAddIpRangeToDifferentL3ButSameL2() {
        InstanceOfferingSpec  ioSpec= env.specByName("instanceOffering")
        L3NetworkInventory l3 = env.inventoryByName("l3")
        L3NetworkInventory l3_1 = env.inventoryByName("l3-1")
        ImageSpec iSpec = env.specByName("image1")

        expect (AssertionError.class) {
            addIpRangeByNetworkCidr {
                name = "cidr-1"
                l3NetworkUuid = l3_1.getUuid()
                networkCidr = "192.168.100.0/24"
            }
        }

        expect (AssertionError.class) {
            addIpRangeByNetworkCidr {
                name = "cidr-2"
                l3NetworkUuid = l3_1.getUuid()
                networkCidr = "192.168.0.0/16"
            }
        }

        expect (AssertionError.class) {
            addIpRangeByNetworkCidr {
                name = "cidr-3"
                l3NetworkUuid = l3_1.getUuid()
                networkCidr = "192.168.100.0/25"
            }
        }

        addIpRange {
            name = "ipr-3"
            l3NetworkUuid = l3_1.getUuid()
            startIp = "192.168.100.101"
            endIp = "192.168.100.200"
            gateway = "192.168.100.1"
            netmask = "255.255.255.0"
        }

        L3NetworkInventory l3_2 = createL3Network {
            name = "l3-2"
            l2NetworkUuid = l3_1.l2NetworkUuid
            category = L3NetworkCategory.Private
        }

        addIpRange {
            name = "ipr-4"
            l3NetworkUuid = l3_2.getUuid()
            startIp = "192.168.101.101"
            endIp = "192.168.101.200"
            gateway = "192.168.101.1"
            netmask = "255.255.255.0"
        }

        L3NetworkInventory l3_3 = createL3Network {
            name = "l3-3"
            l2NetworkUuid = l3_1.l2NetworkUuid
            category = L3NetworkCategory.Private
        }

        addIpRange {
            name = "ipr-5"
            l3NetworkUuid = l3_3.getUuid()
            startIp = "10.0.1.101"
            endIp = "10.0.1.200"
            gateway = "10.0.1.1"
            netmask = "255.0.0.0"
        }

        expect (AssertionError.class) {
            addIpRange {
                name = "ipr-6"
                l3NetworkUuid = l3_3.getUuid()
                startIp = "10.0.2.101"
                endIp = "10.0.2.200"
                gateway = "10.0.2.1"
                netmask = "255.0.0.0"
            }
        }

        CheckIpAvailabilityAction check = new CheckIpAvailabilityAction()
        check.ip = "10.0.1.1"
        check.l3NetworkUuid = l3_3.getUuid()
        check.sessionId = adminSession()
        CheckIpAvailabilityAction.Result res = check.call()
        assert res.value.available == false
        assert res.value.reason == IpNotAvailabilityReason.GATEWAY.toi18nString()

        check = new CheckIpAvailabilityAction()
        check.ip = "10.0.1.2"
        check.l3NetworkUuid = l3_3.getUuid()
        check.sessionId = adminSession()
        res = check.call()
        assert res.value.available == false
        assert res.value.reason == IpNotAvailabilityReason.NO_IN_RANGE.toi18nString()

        check = new CheckIpAvailabilityAction()
        check.ip = "10.0.1.120"
        check.l3NetworkUuid = l3_3.getUuid()
        check.sessionId = adminSession()
        res = check.call()
        assert res.value.available == true

        VmInstanceInventory vm = createVmInstance {
            name = "vm"
            instanceOfferingUuid = ioSpec.inventory.uuid
            imageUuid = iSpec.inventory.uuid
            l3NetworkUuids = asList((l3_3.uuid))
        }
        VmNicInventory nic = vm.getVmNics().get(0)

        check = new CheckIpAvailabilityAction()
        check.ip = nic.ip
        check.l3NetworkUuid = l3_3.getUuid()
        check.sessionId = adminSession()
        res = check.call()
        assert res.value.available == false
        assert res.value.reason == IpNotAvailabilityReason.USED.toi18nString()
    }
}

