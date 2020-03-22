package com.light.common;

import java.util.List;

/**
 * request sending to remote call server
 *
 * @author lihb
 */
public final class Request {
    /**
     * id of a message sending to remote call server
     */
    private long msgId;
    /**
     *
     */
    private int remoteCallInterfaceId;
    /**
     * name of a remote call method
     */
    private String methodName;
    /**
     * arguments of a remote call method
     */
    private List<byte[]> args;

    public long getMsgId() {
        return msgId;
    }

    public void setMsgId(long msgId) {
        this.msgId = msgId;
    }

    public long getRemoteCallInterfaceId() {
        return remoteCallInterfaceId;
    }

    public void setRemoteCallInterfaceId(int remoteCallInterfaceId) {
        this.remoteCallInterfaceId = remoteCallInterfaceId;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public List<byte[]> getArgs() {
        return args;
    }

    public void setArgs(List<byte[]> args) {
        this.args = args;
    }
}
