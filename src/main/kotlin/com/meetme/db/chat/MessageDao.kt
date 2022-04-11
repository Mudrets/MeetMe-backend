package com.meetme.db.chat

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository("messageRepository")
interface MessageDao : JpaRepository<Message, Long>