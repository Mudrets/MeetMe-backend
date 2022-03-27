package com.meetme.invitation.participant

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository("UserInvitationRepository")
interface InvitationDao : JpaRepository<Invitation, Long>