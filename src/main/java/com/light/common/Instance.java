package com.light.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicate instance of a remote call interface  on {@code RemoteCallServer} side
 *
 * @author lihb
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Instance {
    /**
     * class object of a instance of a remote call interface
     *
     * @return
     */
    Class instanceClass();
}
