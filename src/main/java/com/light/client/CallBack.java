package com.light.client;

/**
 * define a callback of a remote call
 *
 * @param <T> the type of result
 * @author lihb
 */
public interface CallBack<T> {
    /**
     * called when a remote call returned
     *
     * @param result result of a remote call
     */
    void onReceive(T result);

    void onTimeout();

    /**
     * obtain the result of a remote call
     */
    T getReturnValue();
}
