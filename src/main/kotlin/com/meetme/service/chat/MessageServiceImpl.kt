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

/**
 * Сервис для работы с сообщениями.
 */
@Service
class MessageServiceImpl @Autowired constructor(
    /**
     * Data access object для взаимодействия с таблицей сообщений в базе данных.
     */
    private val messageDao: MessageDao,
    /**
     * Сервис для работы с пользователем.
     */
    private val userService: UserService,
) : MessageService {

    /**
     * Логгер для логгирования.
     */
    private val logger = LoggerFactory.getLogger(MessageService::class.java)

    /**
     * Создает новое сообщение по переданным данным и сохраняет его
     * в нужном чате.
     * @param content сожержание сообщения.
     * @param userId идентификатор пользователя, отправившего сообщение.
     * @param chat чат, в которое было отправлено сообщение.
     * @return Возвращает отправленное сообщение.
     */
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

    /**
     * Получает сообщение по его идентификатору.
     * @param messageId id сообщения.
     * @return Возвращает сообщение с переданным идентификатором.
     */
    override fun getMessage(messageId: Long): Message =
        messageId.doIfExist(messageDao, logger) { message ->
            message
        }

    /**
     * Изменяет содержание сообщения с переданным идентификатором.
     * @param messageId идентификатор изменяемого сообщения.
     * @param newContent новое содержание сообения.
     */
    override fun editMessage(messageId: Long, newContent: String) =
        messageId.doIfExist(messageDao, logger) { message ->
            message.content = newContent
            messageDao.save(message)
            Unit
        }

    /**
     * Удаляет сообщение с переданным идентификатором.
     * @param messageId идентификатор сообщения.
     */
    override fun deleteMessage(messageId: Long) =
        messageDao.deleteById(messageId)

}