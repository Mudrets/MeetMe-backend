package com.meetme.db.group

import com.meetme.db.meeting.Meeting
import javax.persistence.*

@Entity
class Post(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0,

    @ManyToOne(targetEntity = Meeting::class)
    val meeting: Meeting = Meeting(),

    @ManyToOne(targetEntity = Group::class)
    val group: Group = Group(),
)