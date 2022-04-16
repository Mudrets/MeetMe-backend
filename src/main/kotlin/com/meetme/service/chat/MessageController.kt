package com.meetme.service.chat

import com.meetme.service.chat.mapper.MessageToMessageDto
import com.meetme.domain.dto.DataResponse
import com.meetme.domain.dto.chat.GetMessagesRequestDto
import com.meetme.domain.dto.chat.MessageDto
import com.meetme.domain.dto.chat.SendMessageRequestDto
import com.meetme.util.tryExecute
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

/**
 * Контроллер, обрабатывающий запросы для работы с сообщениями.
 */
@RestController
@RequestMapping("/api/v1/chat/messages")
class MessageController @Autowired constructor(
    /**
     * Сервис для работы с сообщениями.
     */
    private val messageService: MessageService,
    /**
     * Маппер, преобразующий Message в MessageDto.
     */
    private val messageToMessageDto: MessageToMessageDto,
    /**
     * Сервис для работы с чатом.
     */
    private var chatService: ChatService,
) {
    /**
     * Обработчик HTTP POST запроса по url /api/v1/chat/messages/get для получения
     * списка сообщений.
     */
    @PostMapping("/get")
    fun getMessages(
        @RequestBody getMessagesRequestDto: GetMessagesRequestDto,
    ): DataResponse<List<MessageDto>> =
        tryExecute {
            chatService.getMessages(getMessagesRequestDto)
                .map(messageToMessageDto)
        }

    /**
     * Обработчик HTTP POST запроса по url /api/v1/chat/messages для отправления сообщения.
     */
    @PostMapping
    fun sendMessage(
        @RequestBody sendMessageRequestDto: SendMessageRequestDto,
    ): DataResponse<Long> =
        tryExecute { chatService.sendMessage(sendMessageRequestDto) }

    /**
     * Обработчик HTTP DELETE запроса по url /api/v1/chat/messages для удаления сообщения.
     */
    @DeleteMapping("/{message_id}")
    fun deleteMessage(
        @PathVariable("message_id") messageId: Long,
    ): DataResponse<Unit?> =
        tryExecute {
            chatService.deleteMessage(messageId)
            null
        }

    /**
     * Обработчик HTTP PATCH запроса по url /api/v1/chat/messages для редактирования сообщения.
     */
    @PatchMapping("/{message_id}")
    fun editMessage(
        @PathVariable("message_id") messageId: Long,
        @RequestParam("content") content: String,
    ): DataResponse<Unit?> =
        tryExecute {
            messageService.editMessage(messageId, content)
            null
        }
}