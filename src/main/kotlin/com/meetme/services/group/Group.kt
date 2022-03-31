package com.meetme.services.group

import com.meetme.domain.filter.entity.FilteredByInterests
import com.meetme.domain.filter.entity.FilteredByName
import com.meetme.services.auth.User
import com.meetme.services.iterest.Interest
import com.meetme.services.medialink.MediaLink
import com.meetme.services.meeting.Meeting
import javax.persistence.*

@Entity(name = "Groups")
class Group(
    @Id
    @SequenceGenerator(
        name = "group_sequence",
        sequenceName = "group_sequence",
        allocationSize = 1,
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "group_sequence"
    )
    @Column(name = "group_id")
    val id: Long = 0,

    @Column(name = "name")
    var name: String = "",

    @Column(name = "description")
    var description: String? = null,

    @Column(name = "photo_url")
    var photoUrl: String? = null,

    @Column(name = "is_private")
    var private: Boolean = false,

    @ManyToOne(targetEntity = User::class)
    val admin: User = User(),

    @ManyToMany(targetEntity = Meeting::class)
    @JoinColumn(name = "meeting_id")
    @JoinTable(
        name = "meetings_of_group",
        joinColumns = [JoinColumn(name = "group_id")],
        inverseJoinColumns = [JoinColumn(name = "meeting_id")],
    )
    var meetings: MutableList<Meeting> = mutableListOf(),

    @Column(name = "participants")
    @ManyToMany(targetEntity = User::class)
    @JoinTable(
        name = "participants_of_group",
        joinColumns = [JoinColumn(name = "group_id")],
        inverseJoinColumns = [JoinColumn(name = "user_id")],
    )
    var participants: MutableList<User> = mutableListOf(admin),

    @ManyToMany(targetEntity = Interest::class)
    @JoinTable(
        name = "interests_of_group",
        joinColumns = [JoinColumn(name = "group_id")],
        inverseJoinColumns = [JoinColumn(name = "interest_id")],
    )
    var interests: Set<Interest> = mutableSetOf(),

    @OneToMany(targetEntity = MediaLink::class, mappedBy = "group")
    var socialMediaLinks: Set<MediaLink> = mutableSetOf()
) : FilteredByName, FilteredByInterests {

    override val filteredInterests: List<String>
        get() = interests.map(Interest::name)

    override val filteredName: String
        get() = name

}