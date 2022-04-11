package com.meetme.service.chat.mapper

import com.meetme.domain.dto.chat.MessageDto
import com.meetme.db.chat.Message

interface MessageToMessageDto : (Message) -> MessageDto

class MessageToMessageDtoImpl : MessageToMessageDto {
    override fun invoke(message: Message): MessageDto =
        MessageDto(
            id = message.id,
            avatarUrl = message.sender.photoUrl,
            content = message.content,
            senderFullName = message.sender.fullName,
            senderId = message.sender.id,
            timestamp = message.timestamp,
        )

}