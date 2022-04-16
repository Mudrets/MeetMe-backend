package com.meetme.db.meeting

import org.springframework.data.jpa.repository.JpaRepository

/**
 * Интерфейс предоставляющий абстракцию для работы с таблицей мероприятий в базе данных.
 */
interface MeetingDao: JpaRepository<Meeting, Long>