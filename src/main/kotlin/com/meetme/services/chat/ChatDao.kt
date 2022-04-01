package com.meetme.services.chat

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository("chatRepository")
interface ChatDao : JpaRepository<Chat, Long> {
}