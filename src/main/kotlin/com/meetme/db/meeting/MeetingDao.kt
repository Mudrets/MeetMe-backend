package com.meetme.db.meeting

import org.springframework.data.jpa.repository.JpaRepository

interface MeetingDao: JpaRepository<Meeting, Long> {
    fun findAllByPrivate(isPrivate: Boolean): List<Meeting>
}