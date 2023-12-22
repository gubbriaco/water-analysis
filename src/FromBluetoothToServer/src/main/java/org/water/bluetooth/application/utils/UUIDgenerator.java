package org.water.bluetooth.application.utils;

import javax.bluetooth.UUID;

public class UUIDgenerator {

    private static UUID uuid = null;

    private UUIDgenerator() {}

    public static synchronized void generate() {
        if(uuid == null)
            uuid = new UUID(
                    "0000110100001000800000805F9B34FB",
                    false
            );
    }

    public static synchronized UUID getUUID() {
        return uuid;
    }

}
