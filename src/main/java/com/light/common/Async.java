package com.light.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * indicate performing a remote call in a async way on {@code RemoteCallClient} side
 *
 * @author lihb
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Async {
    /**
     * class object of a callBack to receive result  from async remote call
     * callBack instance must implements {@code CallBack}
     *
     * @return
     */
    Class<?> callBack();
}
