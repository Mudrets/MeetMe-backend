package com.meetme.service.chat

import com.meetme.db.chat.Chat
import com.meetme.db.chat.ChatDao
import com.meetme.db.chat.Message
import com.meetme.util.doIfExist
import com.meetme.domain.dto.chat.GetMessagesRequestDto
import com.meetme.domain.dto.chat.SendMessageRequestDto
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import kotlin.math.min

/**
 * Реализация сервис для работы с чатом.
 */
@Service
class ChatServiceImpl @Autowired constructor(
    /**
     * Data access object для работы с базой данных чатов.
     */
    private var chatDao: ChatDao,
    /**
     * Data access object сервис для работы с сообщениями.
     */
    private var messageService: MessageService,
) : ChatService {

    /**
     * Логгер для логгирования.
     */
    private val logger = LoggerFactory.getLogger(ChatServiceImpl::class.java)

    /**
     * Создает чат.
     * @return возвращает созданный чат.
     */
    override fun createChat(): Chat {
        val newChat = Chat()
        return chatDao.save(newChat)
    }

    /**
     * Удаляет чат.
     * @param chat удаляемый чат.
     */
    override fun deleteChat(chat: Chat) {
        chatDao.delete(chat)
    }

    /**
     * Отправляет сообщение в чат.
     * @param sendMessageRequestDto класс с информацией об отправляемом сообщении.
     * @return Возвращает отправляемый id.
     */
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

    /**
     * Получает список сообщений.
     * @param requestData класс с информацией для получения списка сообщений.
     * @return Возвращает список искомых сообщений.
     */
    override fun getMessages(requestData: GetMessagesRequestDto): List<Message> =
        requestData.chatId.doIfExist(chatDao, logger) { chat ->
            with(requestData) {
                getMessageList(chat, anchor, messagesNumber)
            }
        }

    /**
     * Удаляет сообщение по его id.
     * @param messageId id сообщения.
     */
    override fun deleteMessage(messageId: Long) {
        val message = messageService.getMessage(messageId)
        val chat = message.chat
        chat.messages.remove(message)
        messageService.deleteMessage(messageId)
        chatDao.save(chat)
    }

    /**
     * Получает список сообщений.
     * @param chat чат, из которого достаются сообщения.
     * @param anchor Идентификатор последнего загруженного сообщения.
     * Если будет передан 0, то будут возвращены последние
     * сообщения чата.
     * @param messagesNumber количество получаемых сообщений.
     * @return Возвращает список сообщений.
     */
    private fun getMessageList(chat: Chat, anchor: Long, messagesNumber: Int): List<Message> {
        if (messagesNumber < 0)
            throw IllegalArgumentException("messagesNumber must be not negative")
        val reversedMessages = chat.messages.sortedByDescending(Message::timestamp)
        if (anchor == 0L) {
            val endIndex = min(messagesNumber, reversedMessages.size)
            return reversedMessages.subList(0, endIndex)
        }

        val startIndex = reversedMessages.indexOfFirst { message -> message.id == anchor }
        val endIndex = min(startIndex + messagesNumber + 1, reversedMessages.size)
        return reversedMessages.subList(startIndex + 1, endIndex)
    }
}