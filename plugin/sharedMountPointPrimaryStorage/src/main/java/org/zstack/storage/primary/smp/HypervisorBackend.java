package org.zstack.storage.primary.smp;

import org.zstack.header.cluster.ClusterConnectionStatus;
import org.zstack.header.core.Completion;
import org.zstack.header.core.ReturnValueCompletion;
import org.zstack.header.host.CheckSnapshotOnHypervisorReply;
import org.zstack.header.image.ImageInventory;
import org.zstack.header.storage.primary.*;

/**
 * Created by frank on 6/30/2015.
 */
public abstract class HypervisorBackend extends SMPPrimaryStorageBase {
    public HypervisorBackend() {
    }

    public HypervisorBackend(PrimaryStorageVO self) {
        super(self);
    }

    abstract void handle(InstantiateVolumeOnPrimaryStorageMsg msg, ReturnValueCompletion<InstantiateVolumeOnPrimaryStorageReply> completion);

    abstract void handle(DeleteVolumeOnPrimaryStorageMsg msg, ReturnValueCompletion<DeleteVolumeOnPrimaryStorageReply> completion);

    abstract void handle(DownloadDataVolumeToPrimaryStorageMsg msg, ReturnValueCompletion<DownloadDataVolumeToPrimaryStorageReply> completion);

    abstract void handle(GetInstallPathForDataVolumeDownloadMsg msg, ReturnValueCompletion<GetInstallPathForDataVolumeDownloadReply> completion);

    abstract void handle(DeleteVolumeBitsOnPrimaryStorageMsg msg, ReturnValueCompletion<DeleteVolumeBitsOnPrimaryStorageReply> completion);

    abstract void handle(DeleteBitsOnPrimaryStorageMsg msg, ReturnValueCompletion<DeleteBitsOnPrimaryStorageReply> completion);

    abstract void handle(GetPrimaryStorageFolderListMsg msg, ReturnValueCompletion<GetPrimaryStorageFolderListReply> completion);

    abstract void handle(DownloadIsoToPrimaryStorageMsg msg, ReturnValueCompletion<DownloadIsoToPrimaryStorageReply> completion);

    abstract void handle(DeleteIsoFromPrimaryStorageMsg msg, ReturnValueCompletion<DeleteIsoFromPrimaryStorageReply> completion);

    abstract void handle(TakeSnapshotMsg msg, ReturnValueCompletion<TakeSnapshotReply> completion);

    abstract void handle(DeleteSnapshotOnPrimaryStorageMsg msg, ReturnValueCompletion<DeleteSnapshotOnPrimaryStorageReply> completion);

    abstract void handle(RevertVolumeFromSnapshotOnPrimaryStorageMsg msg, ReturnValueCompletion<RevertVolumeFromSnapshotOnPrimaryStorageReply> completion);

    abstract void handle(ReInitRootVolumeFromTemplateOnPrimaryStorageMsg msg, ReturnValueCompletion<ReInitRootVolumeFromTemplateOnPrimaryStorageReply> completion);

    abstract void handle(CreateVolumeFromVolumeSnapshotOnPrimaryStorageMsg msg, ReturnValueCompletion<CreateVolumeFromVolumeSnapshotOnPrimaryStorageReply> completion);

    abstract void handle(MergeVolumeSnapshotOnPrimaryStorageMsg msg, ReturnValueCompletion<MergeVolumeSnapshotOnPrimaryStorageReply> completion);

    abstract void deleteBits(String path, Completion completion);

    abstract void deleteBits(String path, boolean folder, Completion completion);

    abstract void handle(CreateTemplateFromVolumeOnPrimaryStorageMsg msg, ReturnValueCompletion<CreateTemplateFromVolumeOnPrimaryStorageReply> completion);

    abstract void handle(UploadBitsToBackupStorageMsg msg, ReturnValueCompletion<UploadBitsToBackupStorageReply> completion);

    abstract void handleHypervisorSpecificMessage(SMPPrimaryStorageHypervisorSpecificMessage msg);

    abstract void connectByClusterUuid(String clusterUuid, ReturnValueCompletion<ClusterConnectionStatus> completion);

    abstract void handle(SyncVolumeSizeOnPrimaryStorageMsg msg, ReturnValueCompletion<SyncVolumeSizeOnPrimaryStorageReply> completion);

    abstract void handle(CreateTemporaryVolumeFromSnapshotMsg msg, ReturnValueCompletion<CreateTemporaryVolumeFromSnapshotReply> completion);

    abstract void handle(BackupVolumeSnapshotFromPrimaryStorageToBackupStorageMsg msg, ReturnValueCompletion<BackupVolumeSnapshotFromPrimaryStorageToBackupStorageReply> completion);

    abstract void handle(AskInstallPathForNewSnapshotMsg msg, ReturnValueCompletion<AskInstallPathForNewSnapshotReply> completion);

    abstract void handle(CheckVolumeSnapshotsOnPrimaryStorageMsg msg, ReturnValueCompletion<CheckSnapshotOnHypervisorReply> completion);

    abstract void downloadImageToCache(ImageInventory img, final ReturnValueCompletion<String> completion);
}
