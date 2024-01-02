package org.water.bluetooth.application.utils;

import javax.bluetooth.UUID;

/**
 * The {@code UUIDgenerator} class provides functionality for generating and accessing Universally Unique Identifiers
 * (UUIDs). A UUID is a globally unique 128-bit (16-byte) number used to identify profiles, services, and data types in
 * a Generic Attribute (GATT) profile.
 *
 * <p>
 * This class contains methods to generate a UUID and retrieve the generated UUID. The UUID is created with a specific
 * value and flags, and it is used in the context of the Bluetooth Server application.
 * </p>
 *
 * <p>
 * The class is designed to be static, ensuring that the UUID generation and retrieval functionality is easily
 * accessible without the need to instantiate an object.
 * </p>
 *
 * @see javax.bluetooth.UUID
 *
 * @version 1.0
 * @since 2023-12-09
 * @author gubbriaco
 * @author agrandinetti
 * @author fnicoletti
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
        if (uuid == null)
            uuid = new UUID(
                    "0000110100001000800000805F9B34FB",
                    false
            );
    }

    /**
     * Returns the generated Universally Unique Identifier (UUID).
     *
     * @return Universally Unique Identifier (UUID)
     */
    public static synchronized UUID getUUID() {
        return uuid;
    }
}
