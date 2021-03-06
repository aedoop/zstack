package org.zstack.header.storage.backup;

import org.zstack.header.message.NeedReplyMessage;

/**
 * Created by mingjian.deng on 2018/12/25.
 */
public class CheckInstallPathOnBSMsg extends NeedReplyMessage implements BackupStorageMessage {
    private String backupStorageUuid;
    private String installPath;

    @Override
    public String getBackupStorageUuid() {
        return backupStorageUuid;
    }

    public void setBackupStorageUuid(String backupStorageUuid) {
        this.backupStorageUuid = backupStorageUuid;
    }

    public String getInstallPath() {
        return installPath;
    }

    public void setInstallPath(String installPath) {
        this.installPath = installPath;
    }
}
