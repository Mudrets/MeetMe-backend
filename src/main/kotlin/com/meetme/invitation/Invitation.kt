package com.meetme.invitation

import com.meetme.group.Group
import com.meetme.meeting.Meeting
import javax.persistence.*

@Entity(name = "Invitation")
data class Invitation(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invitation_name")
    val id: Long = 0,

    @ManyToOne(targetEntity = Meeting::class, fetch = FetchType.EAGER)
    val meeting: Meeting? = null,

    @ManyToOne(targetEntity = Group::class, fetch = FetchType.EAGER)
    val group: Group? = null,

    @Column(name = "is_column")
    var isAccepted: Boolean = false,

    @Column(name = "is_canceled")
    var isCanceled: Boolean = false,
)