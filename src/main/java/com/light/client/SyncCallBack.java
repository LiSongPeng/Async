package com.light.client;

/**
 * define callback of a sync remote call
 *
 * @author lihb
 */
public class SyncCallBack extends AbstractCallBack {
    @Override
    public void onReceive(Object result) {
        synchronized (this) {
            super.onReceive(result);
            this.notifyAll();
        }
    }
}
