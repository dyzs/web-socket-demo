package com.dyzs.wsdemo.bean;

import java.io.Serializable;

public class WsMessage implements Serializable, Cloneable {

    public String id;

    public String senderUserName;

    public String toName;

    public String msgContent;

    public long date = 0L;

    public WsMessageType messageType = WsMessageType.TYPE_CHAT;


    public enum WsMessageType  {
        TYPE_CHAT, TYPE_CHAT_RECEIVED_FROM_OTHER_USER, TYPE_SYS, TYPE_SYS_NOTIFY_USER_ONLINE
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
