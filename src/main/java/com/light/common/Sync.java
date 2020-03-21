package com.light.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * indicate performing a remote call in a sync way on {@code RemoteCallClient} side
 *
 * @author lihb
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Sync {
    /**
     * wait time
     * 0 stand for wait until a remote call returned
     *
     * @return
     */
    long timeOut() default 0;
}
