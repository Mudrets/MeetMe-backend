package com.meetme.chat

import com.meetme.chat.mapper.MessageToMessageDto
import com.meetme.domain.dto.DataResponse
import com.meetme.domain.dto.chat.MessageDto
import com.meetme.domain.dto.chat.SendMessageRequestDto
import com.meetme.tryExecute
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller

@Controller
class MessageWebSocketController {

    @Autowired
    private lateinit var chatService: ChatService

    @Autowired
    private lateinit var messageService: MessageService

    @Autowired
    private lateinit var messageToMessageDto: MessageToMessageDto

    @MessageMapping("/messages")
    @SendTo("/api/v1/chat/received")
    fun sendMessage(sendMessageRequestDto: SendMessageRequestDto): DataResponse<MessageDto> =
        tryExecute {
            val messageId = chatService.sendMessage(sendMessageRequestDto)
            messageToMessageDto(messageService.getMessage(messageId))
        }
}