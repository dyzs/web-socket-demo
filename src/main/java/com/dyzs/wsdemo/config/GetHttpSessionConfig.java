package com.dyzs.wsdemo.config;

import com.dyzs.wsdemo.utils.Constant;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.HandshakeResponse;
import jakarta.websocket.server.HandshakeRequest;
import jakarta.websocket.server.ServerEndpointConfig;

import java.net.URLDecoder;
import java.nio.charset.Charset;

public class GetHttpSessionConfig extends ServerEndpointConfig.Configurator {

    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        super.modifyHandshake(sec, request, response);
        // 传递参数
        if (request.getHeaders().containsKey(Constant.KEY_USER_TOKEN)) {
            String token = request.getHeaders().get(Constant.KEY_USER_TOKEN).get(0);
            token = URLDecoder.decode(token, Charset.defaultCharset());
            sec.getUserProperties().put(Constant.KEY_USER_TOKEN, token);
        }
        // Web 方式存储httpSession
        // HttpSession httpSession = (HttpSession) request.getHttpSession();
        // 将http session 存贮到配置中
        // sec.getUserProperties().put(httpSession.getClass().getName(), httpSession);
    }
}
