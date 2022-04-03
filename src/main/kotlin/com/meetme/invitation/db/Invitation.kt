package com.meetme.invitation.db

import com.fasterxml.jackson.annotation.JsonIgnore
import com.meetme.group.db.Group
import com.meetme.meeting.db.Meeting
import com.meetme.user.db.User
import javax.persistence.*

@Entity(name = "Invitation")
class Invitation(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invitation_id")
    val id: Long = 0,

    @JsonIgnore
    @OneToOne(targetEntity = Meeting::class)
    val meeting: Meeting = Meeting(),

    @JsonIgnore
    @ManyToMany(
        targetEntity = User::class,
        cascade = [CascadeType.REFRESH, CascadeType.MERGE, CascadeType.DETACH, CascadeType.PERSIST]
    )
    @JoinTable(
        name = "invitations_of_user",
        joinColumns = [JoinColumn(name = "invitation_id")],
        inverseJoinColumns = [JoinColumn(name = "user_id")],
    )
    val users: MutableSet<User> = mutableSetOf(),

    @JsonIgnore
    @ManyToMany(
        targetEntity = Group::class,
        cascade = [CascadeType.REFRESH, CascadeType.MERGE, CascadeType.DETACH, CascadeType.PERSIST]
    )
    @JoinTable(
        name = "invitations_of_group",
        joinColumns = [JoinColumn(name = "group_id")],
        inverseJoinColumns = [JoinColumn(name = "invitation_id")],
    )
    val groups: MutableSet<Group> = mutableSetOf()

)