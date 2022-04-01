package com.meetme.services.chat

import com.meetme.domain.dto.chat.GetMessagesRequestDto
import com.meetme.domain.dto.chat.MessageDto
import com.meetme.domain.dto.chat.SendMessageRequestDto
import com.meetme.domain.dto.chat.MessageIdDto
import com.meetme.services.meeting.Meeting

interface ChatService {

    fun createChat(meeting: Meeting): Chat

    fun deleteChat(chat: Chat)

    fun sendMessage(sendMessageRequestDto: SendMessageRequestDto): MessageIdDto

    fun getMessages(requestData: GetMessagesRequestDto): List<MessageDto>

    fun deleteMessage(messageId: Long)
}