package com.light.client;

import com.light.common.Constant;
import com.light.common.ProxyCenter;
import com.light.common.Request;

import javax.security.auth.callback.Callback;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

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
    /**
     * proxy center
     */
    private ProxyCenter proxyCenter;
    /**
     * reserve sending messages
     * key -> message id
     * value -> callback
     */
    private Map<Long, CallBack> messageMap = new ConcurrentHashMap<>();

    private RemoteCallClient() {
    }

    public void addRemoteCallInterface(Class<?> remoteCallInterface) {
        this.proxyCenter.addManagedRemoteCallInterface(remoteCallInterface);
    }

    public void addRemoteCallInterfaces(List<Class<?>> remoteCallInterfaces) {
        this.proxyCenter.addManagedRemoteCallInterfaces(remoteCallInterfaces);
    }

    public String getAutoScanPackage() {
        return autoScanPackage;
    }

    public int getBossGroupThreadNum() {
        return bossGroupThreadNum;
    }

    public int getWorkerGroupThreadNum() {
        return workerGroupThreadNum;
    }

    public int getPort() {
        return port;
    }

    public String getRemoteServer() {
        return remoteServer;
    }

    public ProxyCenter getProxyCenter() {
        return proxyCenter;
    }

    /**
     * sending a remote call request to {@code RemoteCallServer} side
     *
     * @param request  request
     * @param callback called when a remote call finished
     */
    public void sendRequest(Request request, CallBack callback) {

    }

    /**
     * builder for {@code RemoteCallClient}
     */
    public static final class RemoteCallClientBuilder {
        /**
         * package name for auto scan to find interfaces that need proxying
         */
        private String autoScanPackage;

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

        private RemoteCallClientBuilder() {
        }

        private static RemoteCallClientBuilder create() {
            return new RemoteCallClientBuilder();
        }

        public String getAutoScanPackage() {
            return autoScanPackage;
        }

        public void setAutoScanPackage(String autoScanPackage) {
            this.autoScanPackage = autoScanPackage;
        }

        public int getBossGroupThreadNum() {
            return bossGroupThreadNum;
        }

        public void setBossGroupThreadNum(int bossGroupThreadNum) {
            if (bossGroupThreadNum <= 0) {
                throw new RuntimeException("bossGroupThreadNum should be a positive number");
            }
            this.bossGroupThreadNum = bossGroupThreadNum;
        }

        public int getWorkerGroupThreadNum() {
            return workerGroupThreadNum;
        }

        public void setWorkerGroupThreadNum(int workerGroupThreadNum) {
            if (workerGroupThreadNum <= 0) {
                throw new RuntimeException("workerGroupThreadNum should be a positive number");
            }
            this.workerGroupThreadNum = workerGroupThreadNum;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            if (port <= 0) {
                throw new RuntimeException("port should be a positive number");
            }
            this.port = port;
        }

        public String getRemoteServer() {
            return remoteServer;
        }

        public void setRemoteServer(String remoteServer) {
            if (!Pattern.matches(Constant.IPV4_REGEX, remoteServer) &&
                    !Pattern.matches(Constant.IPV6_REGEX, remoteServer)) {
                throw new RuntimeException(String.format("remote server %s is neither a Ipv4 nor a Ipv6 address", remoteServer));
            }
            this.remoteServer = remoteServer;
        }

        /**
         * call this to build {@code RemoteCallClient}
         *
         * @return instance of {@code RemoteCallClient}
         */
        public RemoteCallClient build() {
            RemoteCallClient client = new RemoteCallClient();
            client.autoScanPackage = autoScanPackage;
            client.bossGroupThreadNum = bossGroupThreadNum;
            client.workerGroupThreadNum = workerGroupThreadNum;
            client.port = port;
            client.remoteServer = remoteServer;
            client.proxyCenter = new ProxyCenter(client);
            return client;
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
