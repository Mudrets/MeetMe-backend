package com.meetme.domain.dto.chat

data class GetMessagesRequestDto(
    val anchor: Long,
    val messagesNumber: Int,
    val chatId: Long
)