package com.meetme.config

import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

/**
 * Класс для настройки работы по протоколу WebSocket
 */
@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig : WebSocketMessageBrokerConfigurer {

    /**
     * Устанавливает endpoint-ы для в отправления запросов через протокол WebSocket
     * и endpoint для подписки на ответы сервера.
     */
    override fun configureMessageBroker(config: MessageBrokerRegistry) {
        config.enableSimpleBroker("/api/v1/chat/received")
        config.setApplicationDestinationPrefixes("/api/v1/websocket/chat")
    }

    /**
     * Устанавливает endpoint для handshake перед началом взаимодействия клиента и сервера
     * через протокол WebSocket.
     */
    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("/stomp").withSockJS()
    }
}