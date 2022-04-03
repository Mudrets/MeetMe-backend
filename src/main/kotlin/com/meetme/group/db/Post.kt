package com.meetme.group.db

import com.meetme.group.db.Group
import com.meetme.meeting.db.Meeting
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