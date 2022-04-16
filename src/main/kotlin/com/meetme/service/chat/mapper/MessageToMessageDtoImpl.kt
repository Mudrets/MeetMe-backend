package com.meetme.service.chat.mapper

import com.meetme.db.chat.Message
import com.meetme.domain.dto.chat.MessageDto

/**
 * Преобразует Message в MessageDto.
 */
class MessageToMessageDtoImpl : MessageToMessageDto {
    override fun invoke(message: Message): MessageDto =
        MessageDto(
            id = message.id,
            avatarUrl = message.sender.photoUrl,
            content = message.content,
            senderFullName = message.sender.fullName,
            senderId = message.sender.id,
            date = message.stringDate,
        )
}