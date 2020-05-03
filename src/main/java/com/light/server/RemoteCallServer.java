package com.light.server;

import com.light.client.CallBack;
import com.light.common.ProxyCenter;
import com.light.common.Request;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.List;

/**
 * remote call server.
 *
 * @author lihb
 */
public final class RemoteCallServer {
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
     * proxy center
     */
    private ProxyCenter proxyCenter;
    /**
     * channel future
     */
    private ChannelFuture connectFuture;
    /**
     * channel handler context
     */
    ChannelHandlerContext channelHandlerContext;

    private RemoteCallServer() {
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
        channelHandlerContext.writeAndFlush(request);
    }

    public void stop() {
        try {
            connectFuture.channel().close().sync();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void init() {
        proxyCenter.scanFor(autoScanPackage);
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        connectFuture = serverBootstrap.group(new NioEventLoopGroup(bossGroupThreadNum),
                new NioEventLoopGroup(workerGroupThreadNum)).channel(NioServerSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                    }
                }).bind("0.0.0.0", port);
    }

    public void start() throws InterruptedException {
        init();
        connectFuture.sync();
    }

    /**
     * builder for {@code RemoteCallServer}
     */
    public static final class RemoteCallServerBuilder {
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

        private RemoteCallServerBuilder() {
        }

        private static RemoteCallServerBuilder create() {
            return new RemoteCallServerBuilder();
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

        /**
         * call this to build {@code RemoteCallServer}
         *
         * @return instance of {@code RemoteCallServer}
         */
        public RemoteCallServer build() {
            RemoteCallServer server = new RemoteCallServer();
            server.autoScanPackage = autoScanPackage;
            server.bossGroupThreadNum = bossGroupThreadNum;
            server.workerGroupThreadNum = workerGroupThreadNum;
            server.port = port;
            server.proxyCenter = new ProxyCenter(server);
            return server;
        }
    }

    /**
     * get a instance of {@code RemoteCallServerBuilder}
     *
     * @return instance of {@code RemoteCallSlientBuilder}
     */
    public static RemoteCallServerBuilder newBuilder() {
        return RemoteCallServerBuilder.create();
    }
}
