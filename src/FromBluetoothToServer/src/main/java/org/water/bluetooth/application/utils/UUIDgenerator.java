package org.water.bluetooth.application.utils;

import org.water.bluetooth.application.ServerApplication;

import javax.bluetooth.UUID;

/**
 * Class for generating the UUID. A Universally Unique Identifier (UUID) is a globally unique 128-bit (16-byte) number
 * that is used to identify profiles, services, and data types in a Generic Attribute (GATT) profile.
 * {@link javax.bluetooth.UUID}
 */
public class UUIDgenerator {

    /**
     * Universally Unique Identifier (UUID).
     * {@link javax.bluetooth.UUID}
     */
    private static UUID uuid = null;


    /**
     * The constructor is implemented as private since the instantiation of the {@link UUIDgenerator} class is
     * obtained via the static {@link UUIDgenerator#generate()} method.
     */
    private UUIDgenerator() {}


    /**
     * Generates a Universally Unique Identifier (UUID).
     */
    public static synchronized void generate() {
        if(uuid == null)
            uuid = new UUID(
                    "0000110100001000800000805F9B34FB",
                    false
            );
    }


    /**
     * Returns the generated Universally Unique Identifier (UUID).
     * @return Universally Unique Identifier (UUID)
     */
    public static synchronized UUID getUUID() {
        return uuid;
    }

}
