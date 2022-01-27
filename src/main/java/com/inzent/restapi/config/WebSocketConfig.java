package com.inzent.restapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

//@Configuration - 해당 클래스가 Bean 설정을 할 것
//@EnableWebSocketMessageBroker - WebSocket 서버 활성화하는데 사용
/*implements WebSocketMessageBrokerConfigurer -
클라이언트가 웹 소켓 서버를  연결하는데 사용할 웹 소켓 엔드 포인트 등록.
엔드 포인트 구성에 withSockJS() 사용 -> SockJS는 웹 소켓을 지원하지 않는 브라우저에 폴백 옵션 활성화시 사용
여기서 잠깐! Fallback이란 어떤 기능이 약해지거나 제대로 동작하지 않을 때 대처하는 기능이나 동작*/
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    //1. WebSocket end-point 및 message broker 구성

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry){
        registry.addEndpoint("/ws").withSockJS();
    }

//STOMP(Simple Text Oriented Messaging Protocol - 데이터 교환의 형식과 규칙을 정의하는 메시징 프로토콜
    //STOM을 사용해야 하는 이유 : 특정 주제를 구독한 사용자에게만 메시지를 보내는 방법을 이용하려면 필요

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry){
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/topic");
    }
    //configureMessageBroker 메서드 : 한 클라이언트에서 다른 클라이언트로 메시지를 라우팅하는데 사용될 메시지 브로커 구성
    /* registry.setApplicationDestinationPrefixes("/app");
    * : "/app" 시작되는 메시지가 message-handling methods로 라우팅되어야 하는 걸 명시함 */
    /*registry.enableSimpleBroker("/topic");
    * : "/topic"으로 시작되는 메시지가 메시지 브로커로 라우팅 되도록 정의하며, 메시지 브로커는 특정
    * 주제를 구독하고 있는 연결된 모든 클라이언트에게 메시지를 broadcast함.
    * 브로드캐스팅이란 송신 호스트가 전송한 데이터가 네트워크에 연결된 모든 호스트에 전송되는 방식을 의미함.
    * */
}
