package com.inzent.restapi.controller;

import com.inzent.restapi.model.ChatMessage;
import com.inzent.restapi.model.MessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

//@Component : 자바 클래스를 스프링 빈이라고 표시해줌. 스프링의 component-scanning 기술이 이 클래스를 어플리케이션 컨텍스트에 빈으로 등록시켜줌.
@Component
public class WebSocketEventListener {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    /*EventListener : Bean 등록된 Class의 메서드에 사용 가능하며, 적용된 메서드의 인수로 SessionConnectedEvent와
    * SessionDisconnectEvent가 있으며 ApplicationEvent를 상속 받음.
    * */
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event){
        logger.info("새로운 웹 소켓 연결 수신");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event){
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String username = (String)headerAccessor.getSessionAttributes().get("username");
        if(username != null){
            logger.info("연결이 끊긴 유저:"+username);

            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setType(MessageType.LEAVE);
            chatMessage.setSender(username);

            messagingTemplate.convertAndSend("/topic/public", chatMessage);
        }
    }


}
