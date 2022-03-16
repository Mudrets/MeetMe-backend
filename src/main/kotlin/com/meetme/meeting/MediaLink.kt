package com.meetme.meeting

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

@Entity
data class MediaLink(
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

    @Column(name = "name")
    var name: String? = null,

    @Column(name = "link")
    var link: String = "",

    @JsonIgnore
    @ManyToOne(
        targetEntity = Meeting::class,
        cascade = [CascadeType.ALL]
    )
    @JoinColumn(name = "meeting_id")
    var meeting: Meeting? = null
)