package com.light.client;

/**
 * define a abstract implement
 *
 * @author lihb
 */
public abstract class AbstractCallBack implements CallBack {
    private Object result;

    @Override
    public void onReceive(Object result) {
        this.result = result;
    }

    @Override
    public Object getReturnValue() {
        return result;
    }
}
