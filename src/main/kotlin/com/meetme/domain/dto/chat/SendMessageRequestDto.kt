package com.meetme.domain.dto.chat

data class SendMessageRequestDto(
    val content: String,
    val chatId: Long,
    val senderId: Long
)