package com.meetme.chat

import com.meetme.domain.dto.DataResponse
import com.meetme.domain.dto.chat.GetMessagesRequestDto
import com.meetme.domain.dto.chat.MessageDto
import com.meetme.domain.dto.chat.MessageIdDto
import com.meetme.domain.dto.chat.SendMessageRequestDto
import com.meetme.tryExecute
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/chat/messages")
class MessageController {

    @Autowired
    private lateinit var messageService: MessageService

    @Autowired
    private lateinit var chatService: ChatService

    @PostMapping
    fun getMessages(
        @RequestBody getMessagesRequestDto: GetMessagesRequestDto,
    ): DataResponse<List<MessageDto>> =
        tryExecute { chatService.getMessages(getMessagesRequestDto) }

    @PostMapping
    fun sendMessage(
        @RequestBody sendMessageRequestDto: SendMessageRequestDto,
    ): DataResponse<MessageIdDto> =
        tryExecute { chatService.sendMessage(sendMessageRequestDto) }

    @DeleteMapping("/{message_id}")
    fun deleteMessage(
        @PathVariable("message_id") messageId: Long,
    ): DataResponse<Unit?> =
        tryExecute {
            chatService.deleteMessage(messageId)
            null
        }

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