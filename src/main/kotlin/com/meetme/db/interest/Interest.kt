package com.meetme.db.interest

import com.fasterxml.jackson.annotation.JsonIgnore
import com.meetme.db.meeting.Meeting
import com.meetme.db.user.User
import com.meetme.db.group.Group
import javax.persistence.*

/**
 * Хранит данные об инетерсе. Представляет из себя модель таблицы в базе данных.
 */
@Entity(name = "Interest")
data class Interest(
    /**
     * Идентификатор.
     */
    @Id
    @SequenceGenerator(
        name = "interest_sequence",
        sequenceName = "interest_sequence",
        allocationSize = 1,
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "interest_sequence"
    )
    @Column(name = "interest_id")
    val id: Long = 0,

    /**
     * Название интереса.
     */
    @Column(
        name = "name",
        unique = true,
    )
    val name: String = "",
) {

    /**
     * Сет мероприятий с данным интересом.
     */
    @JsonIgnore
    @ManyToMany(
        targetEntity = Meeting::class,
        mappedBy = "interests"
    )
    var meetings: Set<Meeting> = setOf()

    /**
     * Сет групп с данным интересом.
     */
    @JsonIgnore
    @ManyToMany(
        targetEntity = Group::class,
        mappedBy = "interests"
    )
    var groups: Set<Group> = setOf()

    /**
     * Сет пользователей с данным интересом.
     */
    @JsonIgnore
    @ManyToMany(
        targetEntity = User::class,
        mappedBy = "interests"
    )
    var users: Set<User> = setOf()

    /**
     * Прдоставляет строковое представление интереса.
     */
    override fun toString(): String {
        return "Interest(" +
            "id: $id, " +
            "name: $name" +
            ")"
    }
}