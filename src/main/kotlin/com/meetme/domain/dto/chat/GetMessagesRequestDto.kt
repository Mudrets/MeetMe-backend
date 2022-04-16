package com.meetme.domain.dto.chat

/**
 * Data transfer object для получения списка сообщений
 * из чата.
 */
data class GetMessagesRequestDto(
    /**
     * Идентификатор последнего загруженного сообщения.
     * Если будет передан 0, то будут возвращены последние
     * сообщения чата.
     */
    val anchor: Long,
    /**
     * Количество сообщений.
     */
    val messagesNumber: Int,
    /**
     * Идентификатор чата.
     */
    val chatId: Long
)