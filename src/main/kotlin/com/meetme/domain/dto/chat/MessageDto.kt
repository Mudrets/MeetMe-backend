package com.meetme.domain.dto.chat

/**
 * Data transfer object с информацией о сообщении, необходимой клиентскому приложению.
 */
data class MessageDto(
    /**
     * Ссылка на фотографию пользователя, отправившего сообщения.
     */
    val avatarUrl: String,
    /**
     * Содержимое сообщения.
     */
    val content: String,
    /**
     * Идентификатор сообщения.
     */
    val id: Long,
    /**
     * Полное имя отправителя.
     */
    val senderFullName: String,
    /**
     * Идентификатор отправителя.
     */
    val senderId: Long,
    /**
     * Дата отправления сообщения в формате MM-dd-yyyy HH:mm
     */
    val date: String,
)