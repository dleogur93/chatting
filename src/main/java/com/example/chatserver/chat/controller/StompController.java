package com.example.chatserver.chat.controller;

import com.example.chatserver.chat.dto.ChatMessageDto;
import com.example.chatserver.chat.service.ChatService;
import com.example.chatserver.chat.service.RedisPubSubService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class StompController {

    private final SimpMessageSendingOperations messageTemplate;
    private final ChatService chatService;
    private final RedisPubSubService pubSubService;


//    //DestinationVariable :@MessageMapping 어노테이션으로 정의된 WebSocket Controller 내에서만 사용
//    @MessageMapping("/{roomId}") //클라이언트에서 특정 publish/roomId 형태로 메세지를 발행시 MessageMapping 수신
//    @SendTo("/topic/{roomId}")  //해당 roomId에 메세지를 발행하여 구독중인 클라이언트에게 메세지 전송
//    public String sendMessage(@DestinationVariable Long roomId,String message){
//        System.out.println(message);
//        return message;
//    }

    @MessageMapping("/{roomId}")
    public void sendMessage(@DestinationVariable Long roomId, ChatMessageDto chatMessageDto) throws JsonProcessingException {
        System.out.println(chatMessageDto.getMessage());
        chatService.saveMessage(roomId, chatMessageDto);
        chatMessageDto.setRoomId(roomId);
//        messageTemplate.convertAndSend("/topic/"+roomId, chatMessageDto);
        ObjectMapper objectMapper = new ObjectMapper();
        String message = objectMapper.writeValueAsString(chatMessageDto);
        pubSubService.publish("chat",message);
    }




}
