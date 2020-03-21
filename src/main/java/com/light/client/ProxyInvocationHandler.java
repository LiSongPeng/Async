package com.light.client;

import com.light.common.Async;
import com.light.common.Sync;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * handle  method call of proxy object
 *
 * @author lihb
 */
public class ProxyInvocationHandler implements InvocationHandler {
    private RemoteCallClient client;

    public ProxyInvocationHandler(RemoteCallClient client) {
        this.client = client;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.isAnnotationPresent(Sync.class)) {
            return invokeSync(proxy, method, args);
        }
        if (method.isAnnotationPresent(Async.class)) {
            return invokeAsync(proxy, method, args);
        }
        return null;
    }

    /**
     * process {@code Sync} situation
     *
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    private Object invokeSync(Object proxy, Method method, Object[] args) throws Throwable {
        synchronized (this) {
        }
        return "";
    }

    /**
     * process {@code Async} situation
     *
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    private Object invokeAsync(Object proxy, Method method, Object[] args) throws Throwable {
        return "";
    }
}
