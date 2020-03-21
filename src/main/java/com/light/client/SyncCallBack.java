package com.light.client;

/**
 * define callback of a sync remote call
 *
 * @param <T> type of a remote call result
 * @author lihb
 */
public class SyncCallBack<T> extends AbstractCallBack<T> {
    @Override
    public void onReceive(T result) {
        super.onReceive(result);
    }

    @Override
    public void onTimeout() {

    }
}
