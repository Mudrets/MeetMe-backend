package com.meetme.service.invitation

interface InvitationService {

    fun sendInvitations(ids: List<Long>, meetingId: Long)

    fun acceptInvitation(id: Long, meetingId: Long)

    fun cancelInvitation(id: Long, meetingId: Long)
}