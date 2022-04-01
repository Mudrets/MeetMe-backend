package com.meetme.services.chat.message

import com.meetme.domain.dto.chat.MessageDto
import com.meetme.domain.dto.chat.MessageIdDto
import com.meetme.services.chat.Chat
import com.meetme.services.chat.message.Message

interface MessageService {

    fun sendMessage(content: String, userId: Long, chat: Chat): Message

    fun getMessage(messageId: Long): Message

    fun editMessage(messageId: Long, newContent: String)

    fun deleteMessage(messageId: Long)
}