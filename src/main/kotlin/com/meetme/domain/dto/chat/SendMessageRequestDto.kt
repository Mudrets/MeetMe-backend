package com.meetme.domain.dto.chat

/**
 * Data transfer object для отправления сообщения.
 */
data class SendMessageRequestDto(
    /**
     * Содержимое отправляемого сообщения.
     */
    val content: String,
    /**
     * Идентификатор чата.
     */
    val chatId: Long,
    /**
     * Идентификатор отправителя.
     */
    val senderId: Long
)