package com.billcom.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.billcom.app.handler.ChatWebSocketHandler;
import com.billcom.app.handler.NotifictionWebSocketHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {

    private final static String CHAT_ENDPOINT = "/chat";
    private final static String Notif_ENDPOINT = "/notif";

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(getChatWebSocketHandler(), CHAT_ENDPOINT)
                .setAllowedOrigins("*");
       webSocketHandlerRegistry.addHandler(geNotfiWebSocketHandler(), Notif_ENDPOINT)
        .setAllowedOrigins("*");
    }

   @Bean
    public WebSocketHandler getChatWebSocketHandler(){
        return new ChatWebSocketHandler();
    }
    @Bean
    public WebSocketHandler geNotfiWebSocketHandler(){
        return new NotifictionWebSocketHandler();
    }
}