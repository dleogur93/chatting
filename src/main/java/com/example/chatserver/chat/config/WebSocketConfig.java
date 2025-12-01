//package com.example.chatserver.chat.config;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.socket.config.annotation.EnableWebSocket;
//import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
//import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
//
//@Configuration
//@EnableWebSocket
//@RequiredArgsConstructor
//public class WebSocketConfig implements WebSocketConfigurer {
//
//    private final SimpleWebSocketHandler simpleWebSocketHandler;
//
//
//    @Override
//    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//
//        //  /connect로 url로 websocket연결 요청이 들어오면, 핸들러 클래스가 처리
//        registry.addHandler(simpleWebSocketHandler,"/connect")
//                .setAllowedOrigins("http://localhost:3000");
//    }
//}
