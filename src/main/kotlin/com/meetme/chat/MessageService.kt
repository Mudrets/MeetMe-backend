package com.meetme.chat

import com.meetme.chat.db.Chat
import com.meetme.chat.db.Message

interface MessageService {

    fun sendMessage(content: String, userId: Long, chat: Chat): Message

    fun getMessage(messageId: Long): Message

    fun editMessage(messageId: Long, newContent: String)

    fun deleteMessage(messageId: Long)
}