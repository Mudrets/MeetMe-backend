package com.meetme.services.invitation.participant

import com.fasterxml.jackson.annotation.JsonIgnore
import com.meetme.services.auth.User
import com.meetme.services.meeting.Meeting
import javax.persistence.*

@Entity(name = "UserInvitation")
class Invitation(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_invitation_id")
    val id: Long = 0,

    @JsonIgnore
    @ManyToOne(targetEntity = Meeting::class, fetch = FetchType.EAGER)
    val meeting: Meeting? = null,

    @JsonIgnore
    @ManyToOne(targetEntity = User::class, fetch = FetchType.EAGER)
    val user: User? = null,

    @Column(name = "is_column")
    var isAccepted: Boolean = false,

    @Column(name = "is_canceled")
    var isCanceled: Boolean = false,
)