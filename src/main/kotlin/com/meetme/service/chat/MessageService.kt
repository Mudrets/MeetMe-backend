package com.meetme.service.chat

import com.meetme.db.chat.Chat
import com.meetme.db.chat.Message

/**
 * Сервис для работы с сообщениями.
 */
interface MessageService {

    /**
     * Создает новое сообщение по переданным данным и сохраняет его
     * в нужном чате.
     * @param content сожержание сообщения.
     * @param userId идентификатор пользователя, отправившего сообщение.
     * @param chat чат, в которое было отправлено сообщение.
     * @return Возвращает отправленное сообщение.
     */
    fun sendMessage(content: String, userId: Long, chat: Chat): Message

    /**
     * Получает сообщение по его идентификатору.
     * @param messageId id сообщения.
     * @return Возвращает сообщение с переданным идентификатором.
     */
    fun getMessage(messageId: Long): Message

    /**
     * Изменяет содержание сообщения с переданным идентификатором.
     * @param messageId идентификатор изменяемого сообщения.
     * @param newContent новое содержание сообения.
     */
    fun editMessage(messageId: Long, newContent: String)

    /**
     * Удаляет сообщение с переданным идентификатором.
     * @param messageId идентификатор сообщения.
     */
    fun deleteMessage(messageId: Long)
}