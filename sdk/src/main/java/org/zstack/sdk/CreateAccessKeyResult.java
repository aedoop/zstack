package org.zstack.sdk;

import org.zstack.sdk.AccessKeyInventory;

public class CreateAccessKeyResult {
    public AccessKeyInventory inventory;
    public void setInventory(AccessKeyInventory inventory) {
        this.inventory = inventory;
    }
    public AccessKeyInventory getInventory() {
        return this.inventory;
    }

}
