package com.meetme.db.invitation

import com.meetme.db.meeting.Meeting
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

/**
 * Интерфейс предоставляющий абстракцию для работы с таблицей приглашений в базе данных.
 */
@Repository("UserInvitationRepository")
interface InvitationDao : JpaRepository<Invitation, Long> {
    /**
     * Находит в базе данных приглашение на мерпориятие с id = meetingId.
     */
    @Query("SELECT inv FROM Invitation inv WHERE inv.meeting.id = ?1")
    fun findByMeeting(meetingId: Long): Invitation?
}