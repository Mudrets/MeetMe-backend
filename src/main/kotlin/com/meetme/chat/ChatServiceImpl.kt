package com.meetme.chat

import com.meetme.chat.db.Chat
import com.meetme.chat.db.ChatDao
import com.meetme.chat.db.Message
import com.meetme.chat.mapper.MessageToMessageDto
import com.meetme.doIfExist
import com.meetme.domain.dto.chat.GetMessagesRequestDto
import com.meetme.domain.dto.chat.MessageDto
import com.meetme.domain.dto.chat.SendMessageRequestDto
import com.meetme.meeting.db.Meeting
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import kotlin.math.min

@Service
class ChatServiceImpl : ChatService {

    private val logger = LoggerFactory.getLogger(ChatServiceImpl::class.java)

    @Autowired
    private lateinit var chatDao: ChatDao

    @Autowired
    private lateinit var messageToMessageDto: MessageToMessageDto

    @Autowired
    private lateinit var messageService: MessageService

    override fun createChat(meeting: Meeting): Chat {
        val newChat = Chat()
        return chatDao.save(newChat)
    }

    override fun deleteChat(chat: Chat) {
        chatDao.delete(chat)
    }

    override fun sendMessage(sendMessageRequestDto: SendMessageRequestDto): Long =
        sendMessageRequestDto.chatId.doIfExist(chatDao, logger) { chat ->
            val message = messageService.sendMessage(
                chat = chat,
                content = sendMessageRequestDto.content,
                userId = sendMessageRequestDto.senderId
            )
            chat.messages.add(message)
            chatDao.save(chat)
            message.id
        }

    override fun getMessages(requestData: GetMessagesRequestDto): List<MessageDto> =
        requestData.chatId.doIfExist(chatDao, logger) { chat ->
            with(requestData) {
                getMessageList(chat, anchor, messagesNumber)
                    .map(messageToMessageDto)
            }
        }

    override fun deleteMessage(messageId: Long) {
        val message = messageService.getMessage(messageId)
        val chat = message.chat
        chat.messages.remove(message)
        messageService.deleteMessage(messageId)
        chatDao.save(chat)
    }

    private fun getMessageList(chat: Chat, anchor: Long, messagesNumber: Int): List<Message> {
        val reversedMessages = chat.messages.sortedByDescending(Message::timestamp)
        if (anchor == 0L) {
            val endIndex = min(messagesNumber + 1, reversedMessages.size)
            return reversedMessages.subList(0, endIndex)
        }

        val startIndex = reversedMessages.indexOfFirst { message -> message.id == anchor }
        val endIndex = min(startIndex + messagesNumber + 1, reversedMessages.size)
        return reversedMessages.subList(startIndex + 1, endIndex)
    }
}