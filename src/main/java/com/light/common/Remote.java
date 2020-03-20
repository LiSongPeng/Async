package com.light.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicate which interfaces are used for remote call
 *
 * @author lihb
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Remote {
    /**
     * identifier for a remote call interface.
     * {@code }server and
     * @return
     */
    int identifier();
}
