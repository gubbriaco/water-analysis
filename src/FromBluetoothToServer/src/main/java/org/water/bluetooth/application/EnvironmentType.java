package org.water.bluetooth.application;

/**
 * The {@code EnvironmentType} enumeration is designed to facilitate the recognition of different environments within
 * the application.
 *
 * <p>
 * It defines three constant values representing distinct environment types: {@link #HOME}, {@link #POOL}, and
 * {@link #SEA}. These environment types can be used to configure application behavior based on the specific
 * environment in which the devices are located.
 * </p>
 *
 * @version 1.0
 * @since 2023-12-09
 * @author gubbriaco
 * @author agrandinetti
 * @author fnicoletti
 */
public enum EnvironmentType {

    /**
     * Environment type HOME.
     */
    HOME,

    /**
     * Environment type POOL.
     */
    POOL,

    /**
     * Environment type SEA.
     */
    SEA;

}
