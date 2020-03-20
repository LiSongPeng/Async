package com.light.client;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * remote call client.
 * use this to perform a remote call which is defined in interface
 * which is annotated by {@code Remote} to a {@code RemoteCallServer}
 *
 * @author lihb
 */
public final class RemoteCallClient {
    /**
     * package name for auto scan to find interfaces that need proxying
     */
    private String autoScanPackage;

    /**
     * proxying remote call interfaces
     * key -> class object of remote call interface
     * value -> proxy object that generated
     */
    private Map<Class, Object> proxyingRemoteCallInterfaceMap = new ConcurrentHashMap<>();

    /**
     * number of threads in boss group for netty
     */
    private int bossGroupThreadNum;
    /**
     * number of threads in worker group for netty
     */
    private int workerGroupThreadNum;
    /**
     * port to bind
     */
    private int port;
    /**
     * address of remote server
     */
    private String remoteServer;

    private RemoteCallClient() {
    }

    /**
     * builder for {@code RemoteCallClient}
     */
    public static final class RemoteCallClientBuilder {
        private RemoteCallClientBuilder() {
        }

        private static RemoteCallClientBuilder create() {
            return new RemoteCallClientBuilder();
        }

        /**
         * call this to build {@code RemoteCallClient}
         *
         * @return instance of {@code RemoteCallClient}
         */
        public RemoteCallClient build() {
            return new RemoteCallClient();
        }
    }

    /**
     * get a instance of {@code RemoteCallClientBuilder}
     *
     * @return instance of {@code RemoteCallClientBuilder}
     */
    public static RemoteCallClientBuilder newBuilder() {
        return RemoteCallClientBuilder.create();
    }
}
