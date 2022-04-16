package com.meetme.db.chat

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Интерфейс предоставляющий абстракцию для работы с таблицей сообщений в базе данных.
 */
@Repository("messageRepository")
interface MessageDao : JpaRepository<Message, Long>