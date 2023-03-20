package com.dyzs.wsdemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
public class WsDemoConfig {

    @Bean
    /**
     * 注入 ServerEndpointExporter 对象，自动注册使用（扫描注解）{@link jakarta.websocket.server.ServerEndpoint}
      */
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

}
