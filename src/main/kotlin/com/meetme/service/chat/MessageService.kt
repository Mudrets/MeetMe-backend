package com.meetme.service.chat

import com.meetme.db.chat.Chat
import com.meetme.db.chat.Message

interface MessageService {

    fun sendMessage(content: String, userId: Long, chat: Chat): Message

    fun getMessage(messageId: Long): Message

    fun editMessage(messageId: Long, newContent: String)

    fun deleteMessage(messageId: Long)
}