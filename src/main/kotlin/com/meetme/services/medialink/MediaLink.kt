package com.meetme.services.medialink

import com.fasterxml.jackson.annotation.JsonIgnore
import com.meetme.services.auth.User
import com.meetme.services.group.Group
import com.meetme.services.meeting.Meeting
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
) {

    @JsonIgnore
    @ManyToOne(
        targetEntity = Group::class,
        fetch = FetchType.EAGER,
        cascade = [CascadeType.ALL]
    )
    var group: Group? = null

    @JsonIgnore
    @ManyToOne(
        targetEntity = User::class,
        cascade = [CascadeType.ALL]
    )
    var user: User? = null

    override fun toString(): String {
        return "MeetingLink(" +
            "id: $id, " +
            "name: $name, " +
            "link: $link" +
            ")"
    }
}