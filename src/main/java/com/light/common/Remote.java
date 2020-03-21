package com.light.common;

import java.lang.annotation.*;

/**
 * Indicate which interfaces are used for remote call
 *
 * @author lihb
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Remote {
    /**
     * identifier for a remote call interface
     * used to identify remote call interface through network
     *
     * @return
     */
    int identifier();
}
