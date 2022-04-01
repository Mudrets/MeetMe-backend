package com.meetme.services.image_store

import com.fasterxml.jackson.annotation.JsonIgnore
import com.meetme.services.meeting.Meeting
import javax.persistence.*

@Entity(name = "image")
data class Image(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "image_id")
    val id: Long = 0,

    @Column(name = "photo_url")
    var photoUrl: String = "",

    @JsonIgnore
    @ManyToOne(
        targetEntity = Meeting::class,
        fetch = FetchType.EAGER,
        cascade = [CascadeType.ALL]
    )
    val meeting: Meeting = Meeting()
)