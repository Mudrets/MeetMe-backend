package com.meetme.db.media_link

import com.fasterxml.jackson.annotation.JsonIgnore
import com.meetme.db.group.Group
import com.meetme.db.user.User
import javax.persistence.*

/**
 * Хранит данные о ссылке на социальную сеть. Представляет из себя модель таблицы в базе данных.
 */
@Entity
data class MediaLink(
    /**
     * Идентификатор.
     */
    @Id
    @SequenceGenerator(
        name = "link_sequence",
        sequenceName = "link_sequence",
        allocationSize = 1,
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "link_sequence"
    )
    @Column(name = "media_link_id")
    val id: Long = 0,

    /**
     * Название социальной сети.
     */
    @Column(name = "name")
    var name: String? = null,

    /**
     * Ссылка на социальную сеть.
     */
    @Column(name = "link")
    var link: String = "",
) {

    /**
     * Пользователь, у которого указана эта ссылка на социальную сеть.
     */
    @JsonIgnore
    @ManyToOne(
        targetEntity = User::class,
        cascade = [CascadeType.ALL]
    )
    var user: User? = null

    /**
     * Возвращает строковое предстваление ссылки.
     */
    override fun toString(): String {
        return "MeetingLink(" +
            "id: $id, " +
            "name: $name, " +
            "link: $link" +
            ")"
    }
}