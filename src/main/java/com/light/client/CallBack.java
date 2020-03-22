package com.light.client;

/**
 * define a callback of a remote call
 *
 * @author lihb
 */
public interface CallBack {
    /**
     * called when a remote call returned
     *
     * @param result result of a remote call
     */
    void onReceive(Object result);

    void onTimeout();

    /**
     * obtain the result of a remote call
     */
    Object getReturnValue();
}
