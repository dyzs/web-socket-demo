package com.dyzs.wsdemo.server;

import com.dyzs.wsdemo.MessageManager;
import com.dyzs.wsdemo.bean.WsMessage;
import com.dyzs.wsdemo.config.GetHttpSessionConfig;
import com.dyzs.wsdemo.utils.Constant;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/wsdemo", configurator = GetHttpSessionConfig.class)
@Component
public class WebsocketServerDemo {
    // 用来存储每个连接对应的 endpoint 对象
    private static Map<String, WebsocketServerDemo> onLineUsers = new ConcurrentHashMap<>();
    private static Set<WebsocketServerDemo> onLineUsers2 = new HashSet<>();
    private HttpSession httpSession;
    private Session session;
    private String userToken;

    @OnOpen
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        System.out.println("onOpen");
        this.session = session;

        if (endpointConfig != null && endpointConfig.getUserProperties().containsKey(Constant.KEY_USER_TOKEN)) {
            String userToken = (String) endpointConfig.getUserProperties().get(Constant.KEY_USER_TOKEN);
            this.userToken = userToken;
            onLineUsers.put(userToken, this);
        }

//        onLineUsers2.add(this);

//        // 获取 http session 对象
//        HttpSession httpSession = (HttpSession) endpointConfig.getUserProperties().get(HttpSession.class.getName());
//        this.httpSession = httpSession;
//        // 讲当前对象存储到user中
//        String username = (String) httpSession.getAttribute("user");
//        onLineUsers.put(username, this);
        // 推送上线系统消息
//        WsMessage message = MessageManager.createSysMessage(session.getId(), getLoginNames());
//        String msg = MessageManager.createJSONMessage(message);
        broadcastUserOnline();
    }

    private void broadcastUserOnline() {
        try {
            Set<String> users = onLineUsers.keySet();
            for (String user : users) {
                WebsocketServerDemo wsd = onLineUsers.get(user);
                if (user.equals(this.userToken)) {
                    continue;
                }
                WsMessage message = MessageManager.broadcastUserOnline(user, this.userToken + "上线了");
                String msg = MessageManager.createJSONMessage(message);
                wsd.session.getBasicRemote().sendText(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void broadcastUserLogout() {
        try {
            Set<String> users = onLineUsers.keySet();
            for (String user : users) {
                if (user.equals(this.userToken)) {
                    continue;
                }
                WebsocketServerDemo wsd = onLineUsers.get(user);
                WsMessage message = MessageManager.broadcastUserOnline(user, this.userToken + "下线了");
                String msg = MessageManager.createJSONMessage(message);
                wsd.session.getBasicRemote().sendText(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        try {
            session.close();
            onLineUsers.remove(userToken);
            broadcastUserLogout();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("onClose:" + closeReason.toString());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.out.println("error:" + throwable.toString());
    }

    @OnMessage
    public void onMessage(String message, Session session) throws Exception {
        System.out.println("onMessage");
        ObjectMapper mapper = new ObjectMapper();
        WsMessage receiveMsg = mapper.readValue(message, WsMessage.class);
        sendGroupMsg(receiveMsg);
    }

    private void sendGroupMsg(WsMessage receiveMsg) throws Exception {
        if (receiveMsg == null || onLineUsers.size() == 0) return;
        for (String user : onLineUsers.keySet()) {
            // String userName = (String) httpSession.getAttribute("user");
            receiveMsg.senderUserName = receiveMsg.senderUserName;
            receiveMsg.toName = user;
            receiveMsg.msgContent = receiveMsg.msgContent;
            receiveMsg.messageType = WsMessage.WsMessageType.TYPE_CHAT_RECEIVED_FROM_OTHER_USER;
            onLineUsers.get(user).session.getBasicRemote().sendText(MessageManager.createJSONMessage(receiveMsg));
        }
        // onLineUsers.get(toName).session.getBasicRemote().sendText(wsMessage.msgContent);

    }

    public String getLoginNames() {
        return onLineUsers.keySet().toString();
    }
}
