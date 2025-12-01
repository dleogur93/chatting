package com.example.chatserver.chat.controller;

import com.example.chatserver.chat.dto.ChatMessageDto;
import com.example.chatserver.chat.dto.ChatRoomListResponseDto;
import com.example.chatserver.chat.dto.MyChatListResponseDto;
import com.example.chatserver.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;


    //그룹 채팅방 개설
    @PostMapping("/room/group/create")
    public ResponseEntity<?> createGroup(@RequestParam String roomName) {
        chatService.createGroupRoom(roomName);
        return ResponseEntity.ok().build();
    }

    //그룹 채팅방 목록 조회
    @GetMapping("/room/group/list")
    public ResponseEntity<?> getGroupChatList() {
       List<ChatRoomListResponseDto> chatList= chatService.getGroupChatList();
       return new ResponseEntity<>(chatList, HttpStatus.OK);
    }

    //그룹 채팅방 참여
    @PostMapping("/room/group/{roomId}/join")
    public ResponseEntity<?> joinGroupChatRoom(@PathVariable Long roomId) {
        chatService.addParticipantToGroupChat(roomId);
        return ResponseEntity.ok().build();
    }

    //이전 메세지 조회
    @GetMapping("/history/{roomId}")
    public ResponseEntity<?> getChatHistory(@PathVariable Long roomId) {
        List<ChatMessageDto> chatMessageDtos = chatService.getChatHistory(roomId);
        return new ResponseEntity<>(chatMessageDtos, HttpStatus.OK);
    }

    //채팅 메세지 읽음처리
    @PostMapping("/room/{roomId}/read")
    public ResponseEntity<?> messageRead(@PathVariable Long roomId) {
        chatService.messageRead(roomId);
        return ResponseEntity.ok().build();
    }

    //내 채팅방 목록 조회
    @GetMapping("/my/rooms")
    public ResponseEntity<?> getMyChatRooms() {

        List<MyChatListResponseDto> myChatListResponseDtos = chatService.getMyRooms();

        return new ResponseEntity<>(myChatListResponseDtos,HttpStatus.OK);
    }

    //채팅방 나가기
    @DeleteMapping("/room/group/{roomId}/leave")
    public ResponseEntity<?> leaveGroupChatRoom(@PathVariable Long roomId) {
        chatService.leaveGroupChatRoom(roomId);
        return ResponseEntity.ok().build();
    }

    //개인 채팅방 개설 또는 기존방 return
    @PostMapping("/room/private/create")
    public ResponseEntity<?> getOrCreatePrivateRoom(@RequestParam Long otherMemberId) {
        Long roomId = chatService.getOrCreatePrivateRoom(otherMemberId);
        return new ResponseEntity<>(roomId, HttpStatus.OK);
    }


}
