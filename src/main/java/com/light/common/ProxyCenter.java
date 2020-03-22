package com.light.common;

import com.light.client.ProxyInvocationHandler;
import com.light.client.RemoteCallClient;
import com.light.server.RemoteCallServer;
import com.light.utils.ClassScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * response for load remote call interfaces and generate proxy object for them
 *
 * @author lihb
 */
public final class ProxyCenter {
    private static final Logger logger = LoggerFactory.getLogger(ProxyCenter.class);
    private RemoteCallClient client;
    private RemoteCallServer server;

    /**
     * proxying remote call interfaces
     * key -> class object of remote call interface
     * value -> proxy object that generated
     */
    private Map<Class, Object> remoteCallInterface2ProxyMap = new ConcurrentHashMap<>();
    /**
     * proxying remote call interfaces
     * key -> identifier of remote call interface
     * value -> proxy object that generated
     */
    private Map<Integer, Object> identifier2ProxyMap = new ConcurrentHashMap<>();

    public ProxyCenter(RemoteCallClient client) {
        this.client = client;
    }

    public ProxyCenter(RemoteCallServer server) {
        this.server = server;
    }

    /**
     * scan remote call instances which is annotated by {@code Remote}
     *
     * @param packageName full qualified package name for scanning
     * @return true if loaded successfully or false if failed
     */
    public boolean scanFor(String packageName) {
        Objects.requireNonNull(packageName);
        if (!Pattern.matches(Constant.PACKAGE_NAME_REGEX, packageName)) {
            throw new RuntimeException(String.format("not a valid package name:%s", packageName));
        }
        Set<Class<?>> classSet = ClassScanner.scan(packageName, false);
        List<Class<?>> remoteCallClasses = classSet.stream().filter(clazz -> clazz.isAnnotationPresent(Remote.class) && clazz.isInterface())
                .collect(Collectors.toList());
        addManagedRemoteCallInterfaces(remoteCallClasses);
        return true;
    }

    /**
     * add remote call interface to {@code ProxyCenter} for manage
     *
     * @param remoteCallInterface adding remote call interface
     */
    public void addManagedRemoteCallInterface(Class<?> remoteCallInterface) {
        if (remoteCallInterface2ProxyMap.get(remoteCallInterface) != null) {
            throw new RuntimeException(String.format("interface %s already added", remoteCallInterface));
        }
        Objects.requireNonNull(remoteCallInterface);
        if (!remoteCallInterface.isAnnotationPresent(Remote.class)) {
            throw new RuntimeException(String.format("interface %s is not annotated by %s",
                    remoteCallInterface.getName(), Remote.class.getName()));
        }
        Remote remoteAnnotation = remoteCallInterface.getAnnotation(Remote.class);
        int identifier = remoteAnnotation.identifier();
        if (identifier <= 0) {
            throw new RuntimeException("identifier that Remote annotation assigned should be a positive number");
        }
        Object proxy;
        if (remoteCallInterface.isAnnotationPresent(Instance.class)) {
            Instance instanceAnnotation = remoteCallInterface.getAnnotation(Instance.class);
            Class instanceClass = instanceAnnotation.instanceClass();
            if (instanceClass == null) {
                throw new RuntimeException(String.format("instanceClass property required"));
            }
            if (!remoteCallInterface.isAssignableFrom(instanceClass)) {
                throw new RuntimeException(String.format("Class %s that instanceClass property of annotation Instance assigned not implemented remote call interface %s",
                        instanceClass.getName(), remoteCallInterface.getName()));
            }
            try {
                proxy = instanceClass.getConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException(String.format("Class %s need contain a default constructor",
                        instanceClass.getName()));
            }
            logger.debug("adding remote call interface:%s, identifier:%d, instance:%s", remoteCallInterface.getName(), identifier, instanceClass.getName());
        } else {
            proxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{remoteCallInterface}, new ProxyInvocationHandler(client));
            logger.debug("adding remote call interface:%s, identifier:%d", remoteCallInterface.getName(), identifier);
        }
        identifier2ProxyMap.put(identifier, proxy);
        remoteCallInterface2ProxyMap.put(remoteCallInterface, proxy);
    }

    /**
     * add remote call interfaces to {@code ProxyCenter} for manage
     *
     * @param remoteCallInterfaces adding remote call interfaces
     */
    public void addManagedRemoteCallInterfaces(List<Class<?>> remoteCallInterfaces) {
        Objects.requireNonNull(remoteCallInterfaces);
        for (Class<?> remoteCallInterface : remoteCallInterfaces) {
            addManagedRemoteCallInterface(remoteCallInterface);
        }
    }

    /**
     * obtain a proxy object for a remote call interface
     *
     * @param remoteCallInterfaceClass class of a remote call interface
     * @return proxy object or null if the interface not managed by {@code ProxyCenter}
     */
    public Object getProxy(Class remoteCallInterfaceClass) {
        return remoteCallInterface2ProxyMap.get(remoteCallInterfaceClass);
    }

    /**
     * obtain a proxy object for a remote call interface
     *
     * @param identifier identifier of a remote call interface
     * @return proxy object or null if the interface not managed by {@code ProxyCenter}
     */
    public Object getProxy(int identifier) {
        return identifier2ProxyMap.get(identifier);
    }
}
