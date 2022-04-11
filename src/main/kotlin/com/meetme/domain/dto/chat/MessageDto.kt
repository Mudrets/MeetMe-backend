package com.meetme.domain.dto.chat

data class MessageDto(
    val avatarUrl: String,
    val content: String,
    val id: Long,
    val senderFullName: String,
    val senderId: Long,
    val date: String,
)