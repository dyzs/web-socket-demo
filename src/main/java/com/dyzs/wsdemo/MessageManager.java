package com.dyzs.wsdemo;

import com.dyzs.wsdemo.bean.WsMessage;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MessageManager {

    public MessageManager() {


    }

    public static WsMessage createChatMessage(String fromName, String toName, String messageContent) {
        WsMessage wsMessage = new WsMessage();
        wsMessage.messageType = WsMessage.WsMessageType.TYPE_CHAT;
        wsMessage.senderUserName = fromName;
        wsMessage.date = System.currentTimeMillis();
        wsMessage.msgContent = messageContent;
        wsMessage.toName = toName;
        return wsMessage;
    }

    public static WsMessage createSysMessage(String fromName, String messageContent) {
        WsMessage wsMessage = new WsMessage();
        wsMessage.messageType = WsMessage.WsMessageType.TYPE_SYS;
        wsMessage.senderUserName = fromName;
        wsMessage.date = System.currentTimeMillis();
        wsMessage.msgContent = messageContent;
        wsMessage.toName = "ALL";
        return wsMessage;
    }

    public static WsMessage broadcastUserOnline(String toName, String messageContent) {
        WsMessage wsMessage = new WsMessage();
        wsMessage.messageType = WsMessage.WsMessageType.TYPE_SYS_NOTIFY_USER_ONLINE;
        wsMessage.senderUserName = "USER_SYS";
        wsMessage.date = System.currentTimeMillis();
        wsMessage.msgContent = messageContent;
        wsMessage.toName = toName;
        return wsMessage;
    }

    public static String createJSONMessage(WsMessage wsMessage) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(wsMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
