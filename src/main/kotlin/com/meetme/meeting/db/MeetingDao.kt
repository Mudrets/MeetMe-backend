package com.meetme.meeting.db

import org.springframework.data.jpa.repository.JpaRepository

interface MeetingDao: JpaRepository<Meeting, Long> {
    fun findAllByPrivate(isPrivate: Boolean): List<Meeting>
}