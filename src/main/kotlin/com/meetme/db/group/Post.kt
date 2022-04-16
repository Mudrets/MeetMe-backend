package com.meetme.db.group

import com.meetme.db.meeting.Meeting
import javax.persistence.*

/**
 * Хранит данные о записи об участии группы в мероприятии.
 * Представляет из себя модель таблицы в базе данных.
 */
@Entity
class Post(
    /**
     * Идентификатор записи о участии в мероприятии.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0,

    /**
     * Мероприятие в котором участвует группа.
     */
    @ManyToOne(targetEntity = Meeting::class)
    val meeting: Meeting = Meeting(),

    /**
     * Участвующая в мероприятии группа.
     */
    @ManyToOne(targetEntity = Group::class)
    val group: Group = Group(),
)