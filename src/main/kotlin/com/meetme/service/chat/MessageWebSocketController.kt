package com.meetme.service.chat

import com.meetme.service.chat.mapper.MessageToMessageDto
import com.meetme.domain.dto.DataResponse
import com.meetme.domain.dto.chat.MessageDto
import com.meetme.domain.dto.chat.SendMessageRequestDto
import com.meetme.util.tryExecute
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller

/**
 * Контроллер, обрабатывающий запросы для работы с сообщениями по протоколу WebSocket.
 */
@Controller
class MessageWebSocketController @Autowired constructor(
    /**
     * Сервис для работы с чатом.
     */
    private val chatService: ChatService,
    /**
     * Сервис для работы с сообщениями.
     */
    private val messageService: MessageService,
    /**
     * Маппер, преобразующий Message в MessageDto.
     */
    private val messageToMessageDto: MessageToMessageDto,
) {

    /**
     * Обработчик WebSocket запроса по url /api/v1/websocket/chat/messages для отправления сообщений
     */
    @MessageMapping("/messages")
    @SendTo("/api/v1/chat/received")
    fun sendMessage(sendMessageRequestDto: SendMessageRequestDto): DataResponse<MessageDto> =
        tryExecute {
            val messageId = chatService.sendMessage(sendMessageRequestDto)
            messageToMessageDto(messageService.getMessage(messageId))
        }
}