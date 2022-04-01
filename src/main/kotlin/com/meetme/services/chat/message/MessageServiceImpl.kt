package com.meetme.services.chat.message

import com.meetme.doIfExist
import com.meetme.domain.dto.chat.MessageDto
import com.meetme.mapper.MessageToMessageDto
import com.meetme.services.auth.UserService
import com.meetme.services.chat.Chat
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.Date

@Service
class MessageServiceImpl : MessageService {

    private val logger = LoggerFactory.getLogger(MessageService::class.java)

    @Autowired
    private lateinit var messageDao: MessageDao

    @Autowired
    private lateinit var userService: UserService

    override fun sendMessage(content: String, userId: Long, chat: Chat): Message =
        userId.doIfExist(userService) { user ->
            val newMessage = Message(
                content = content,
                sender = user,
                chat = chat,
                timestamp = Date.from(Instant.now()).time,
            )
            messageDao.save(newMessage)
        }

    override fun getMessage(messageId: Long): Message =
        messageId.doIfExist(messageDao, logger) { message ->
            message
        }

    override fun editMessage(messageId: Long, newContent: String) =
        messageId.doIfExist(messageDao, logger) { message ->
            message.content = newContent
            messageDao.save(message)
            Unit
        }

    override fun deleteMessage(messageId: Long) =
        messageDao.deleteById(messageId)

}