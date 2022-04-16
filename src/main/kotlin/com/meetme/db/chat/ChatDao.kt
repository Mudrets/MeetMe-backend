package com.meetme.db.chat

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Интерфейс предоставляющий абстракцию для работы с таблицей чатов в базе данных.
 */
@Repository("chatRepository")
interface ChatDao : JpaRepository<Chat, Long>