package com.meetme.service.chat

import com.meetme.db.chat.Chat
import com.meetme.db.chat.Message
import com.meetme.domain.dto.chat.GetMessagesRequestDto
import com.meetme.domain.dto.chat.SendMessageRequestDto
import com.meetme.db.meeting.Meeting

/**
 * Сервис для работы с чатом.
 */
interface ChatService {

    /**
     * Создает чат.
     * @return Возращает созданный чат.
     */
    fun createChat(): Chat

    /**
     * Удаляет чат.
     * @param chat удаляемый чат.
     */
    fun deleteChat(chat: Chat)

    /**
     * Отправляет сообщение в чат.
     * @param sendMessageRequestDto класс с информацией об отправляемом сообщении.
     * @return Возвращает отправляемый id.
     */
    fun sendMessage(sendMessageRequestDto: SendMessageRequestDto): Long

    /**
     * Получает список сообщений.
     * @param requestData класс с информацией для получения списка сообщений.
     * @return Возвращает список искомых сообщений.
     */
    fun getMessages(requestData: GetMessagesRequestDto): List<Message>

    /**
     * Удаляет сообщение по его id.
     * @param messageId id сообщения.
     */
    fun deleteMessage(messageId: Long)
}