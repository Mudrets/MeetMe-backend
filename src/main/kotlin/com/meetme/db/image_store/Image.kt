package com.meetme.db.image_store

import com.fasterxml.jackson.annotation.JsonIgnore
import com.meetme.db.meeting.Meeting
import javax.persistence.*

/**
 * Хранит данные об изображении из хранилища фотографий мероприятия.
 * Представляет из себя модель таблицы в базе данных.
 */
@Entity(name = "image")
data class Image(
    /**
     * Идентификатор.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "image_id")
    val id: Long = 0,

    /**
     * Ссылка на изображение.
     */
    @Column(name = "photo_url")
    var photoUrl: String = "",

    /**
     * Мероприятие в хранилище которого хранится изображение.
     */
    @JsonIgnore
    @ManyToOne(
        targetEntity = Meeting::class,
        fetch = FetchType.EAGER,
        cascade = [CascadeType.ALL]
    )
    val meeting: Meeting = Meeting()
)