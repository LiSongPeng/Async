package com.light.client;

/**
 * define a abstract implement
 *
 * @param <T> type of a remote call result
 * @author lihb
 */
public abstract class AbstractCallBack<T> implements CallBack<T> {
    private T result;

    @Override
    public void onReceive(T result) {
        this.result = result;
    }

    @Override
    public T getReturnValue() {
        return result;
    }
}
