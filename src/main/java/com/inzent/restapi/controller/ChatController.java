package com.inzent.restapi.controller;

import com.inzent.restapi.model.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

@Controller
public class ChatController {

    @MessageMapping("/chat.sendMsg")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage){
        return chatMessage;
    }
    //"/app"으로 시작하는 대상이 있는 클라이언트에서 보낸 모든 메시지는 @MessageMapping이 있는 메서드로 라우팅

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccesor){
        headerAccesor.getSessionAttributes().put("username",  chatMessage.getSender());
        return chatMessage;
    }
}
