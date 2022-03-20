package com.meetme.meeting

import com.meetme.iterest.Interest
import com.meetme.medialink.MediaLink
import com.meetme.auth.User
import java.time.Instant
import java.util.Date
import javax.persistence.*

@Entity(name = "Meeting")
data class Meeting(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meeting_name")
    val id: Long = 0,

    @Column(name = "name")
    var name: String? = null,

    @Column(name = "description")
    var description: String? = null,

    @Column(name = "photo_url")
    var imageUrl: String? = null,

    @Column(name = "is_online")
    var isOnline: Boolean = false,

    @Column(name = "start_date")
    var startDate: Date = Date.from(Instant.now()),

    @Column(name = "end_date")
    var endDate: Date? = null,

    @Column(name = "is_private")
    var isPrivate: Boolean = false,

    @Column(name = "location")
    var location: String? = null,

    @ManyToOne(targetEntity = User::class)
    @JoinColumn(name = "user_id")
    val admin: User? = null,

    @ManyToMany(targetEntity = Interest::class)
    @JoinTable(
        name = "interests_of_meeting",
        joinColumns = [JoinColumn(name = "meeting_id")],
        inverseJoinColumns = [JoinColumn(name = "interest_id")],
    )
    var interests: Set<Interest> = mutableSetOf(),

    @Column(name = "participants")
    @ManyToMany(targetEntity = User::class)
    @JoinTable(
        name = "participants_of_meeting",
        joinColumns = [JoinColumn(name = "meeting_id")],
        inverseJoinColumns = [JoinColumn(name = "user_id")],
    )
    val participants: MutableList<User> = mutableListOf(),

    @OneToMany(targetEntity = MediaLink::class, mappedBy = "meeting")
    var socialMediaLinks: Set<MediaLink> = mutableSetOf()
)
