package com.light.client;

import com.light.common.Async;
import com.light.common.Remote;
import com.light.common.Request;
import com.light.common.Sync;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * handle  method call of proxy object
 *
 * @author lihb
 */
public class ProxyInvocationHandler implements InvocationHandler {
    private static final Logger logger = LoggerFactory.getLogger(ProxyInvocationHandler.class);
    private RemoteCallClient client;
    private Random random = new Random();
    private ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
    private ObjectOutputStream objectOutputStream;

    public ProxyInvocationHandler(RemoteCallClient client) {
        this.client = client;
        try {
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        } catch (IOException e) {
            logger.error("", e);
        }
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
        Class<?> remoteCallInterfaceClass = proxy.getClass().getSuperclass();
        if (!remoteCallInterfaceClass.isAnnotationPresent(Remote.class)) {
            throw new RuntimeException(String.format("interface %s is not annotated by Remote", remoteCallInterfaceClass.getName()));
        }
        Remote remote = remoteCallInterfaceClass.getAnnotation(Remote.class);
        if (remote.identifier() <= 0) {
            throw new RuntimeException(String.format("identifier of Remote annotation assigned on %s should be positive", remoteCallInterfaceClass.getName()));
        }
        Sync sync = method.getAnnotation(Sync.class);
        Request request = generateRequest(remote.identifier(), method.getName(), args);
        CallBack callBack = new SyncCallBack();
        client.sendRequest(request, callBack);
        synchronized (callBack) {
            if (sync.timeOut() > 0) {
                callBack.wait(sync.timeOut());
            } else {
                try {
                    callBack.wait();
                } catch (Exception e) {
                    logger.error("", e);
                }
            }
        }
        return callBack.getReturnValue();
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

    /**
     * generate request
     *
     * @param remoteCallInterfaceIdentifier
     * @param methodName
     * @param args
     * @return
     */
    private Request generateRequest(int remoteCallInterfaceIdentifier, String methodName, Object[] args) throws Exception {
        long msgId = System.nanoTime() + random.nextLong();
        Request request = new Request();
        request.setMsgId(msgId);
        request.setRemoteCallInterfaceId(remoteCallInterfaceIdentifier);
        request.setMethodName(methodName);
        List<byte[]> argsList = new LinkedList<>();
        for (Object arg : args) {
            try {
                objectOutputStream.writeObject(arg);
                argsList.add(byteArrayOutputStream.toByteArray());
            } catch (Exception e) {
                objectOutputStream.reset();
                throw e;
            }
        }
        request.setArgs(argsList);
        return request;
    }
}
