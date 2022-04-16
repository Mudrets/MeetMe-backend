package com.meetme.db.invitation

import com.fasterxml.jackson.annotation.JsonIgnore
import com.meetme.db.group.Group
import com.meetme.db.meeting.Meeting
import com.meetme.db.user.User
import javax.persistence.*

/**
 * Хранит данные о приглашении на мероприятие. Представляет из себя модель таблицы в базе данных.
 */
@Entity(name = "Invitation")
class Invitation(
    /**
     * Идентификатор.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invitation_id")
    val id: Long = 0,

    /**
     * Мероприятие, на которое было отправленно приглашение.
     */
    @JsonIgnore
    @OneToOne(targetEntity = Meeting::class, fetch = FetchType.LAZY)
    val meeting: Meeting = Meeting(),

    /**
     * Сет пользователей, которым было отправленно приглашение на мероприятие.
     */
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

    /**
     * Сет групп, которым было отправленно приглашение на мероприятие.
     */
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