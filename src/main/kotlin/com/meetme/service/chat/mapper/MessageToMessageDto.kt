package com.meetme.service.chat.mapper

import com.meetme.db.chat.Message
import com.meetme.domain.dto.chat.MessageDto

/**
 * Преобразует Message в MessageDto.
 */
interface MessageToMessageDto : (Message) -> MessageDto