package com.meetme.service.chat

import com.meetme.db.chat.Chat
import com.meetme.db.chat.Message
import com.meetme.db.chat.MessageDao
import com.meetme.service.user.UserService
import com.meetme.util.doIfExist
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.Date

@Service
class MessageServiceImpl @Autowired constructor(
    private val messageDao: MessageDao,
    private val userService: UserService,
) : MessageService {

    private val logger = LoggerFactory.getLogger(MessageService::class.java)

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