package com.meetme.meeting

import com.meetme.auth.User
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
    var photoUrl: String? = null,

    @ManyToOne(targetEntity = User::class)
    @JoinColumn(name = "user_id")
    val admin: User? = null,

    @ManyToMany
    @JoinTable(
        name = "interests_of_meeting",
        joinColumns = [JoinColumn(name = "meeting_id")],
        inverseJoinColumns = [JoinColumn(name = "interest_id")],
    )
    var interests: Set<Interest> = mutableSetOf(),

    @OneToMany(targetEntity = MediaLink::class, mappedBy = "meeting")
    var socialMediaLinks: Set<MediaLink> = mutableSetOf()
)
