package com.meetme.service.chat

import com.meetme.db.chat.Chat
import com.meetme.db.chat.Message
import com.meetme.domain.dto.chat.GetMessagesRequestDto
import com.meetme.domain.dto.chat.SendMessageRequestDto
import com.meetme.db.meeting.Meeting

interface ChatService {

    fun createChat(): Chat

    fun deleteChat(chat: Chat)

    fun sendMessage(sendMessageRequestDto: SendMessageRequestDto): Long

    fun getMessages(requestData: GetMessagesRequestDto): List<Message>

    fun deleteMessage(messageId: Long)
}