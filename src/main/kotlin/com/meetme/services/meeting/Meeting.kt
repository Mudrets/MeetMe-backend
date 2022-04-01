package com.meetme.services.meeting

import com.fasterxml.jackson.annotation.JsonIgnore
import com.meetme.domain.filter.entity.FilteredByInterests
import com.meetme.domain.filter.entity.FilteredByName
import com.meetme.services.auth.User
import com.meetme.services.chat.Chat
import com.meetme.services.image_store.Image
import com.meetme.services.iterest.Interest
import javax.persistence.*

@Entity(name = "Meeting")
data class Meeting(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meeting_name")
    val id: Long = 0,

    @Column(name = "name")
    var name: String = "",

    @Column(name = "description")
    var description: String? = null,

    @Column(name = "photo_url")
    var photoUrl: String? = null,

    @Column(name = "is_online")
    var isOnline: Boolean = false,

    @Column(name = "start_date")
    var startDate: String = "",

    @Column(name = "end_date")
    var endDate: String? = null,

    @Column(name = "is_private")
    var private: Boolean = false,

    @Column(name = "location")
    var location: String? = null,

    @Column(name = "max_number_of_participants")
    var maxNumberOfParticipants: Int = 1,

    @ManyToOne(targetEntity = User::class)
    @JoinColumn(name = "user_id")
    val admin: User = User(),

    @ManyToMany(targetEntity = Interest::class)
    @JoinTable(
        name = "interests_of_meeting",
        joinColumns = [JoinColumn(name = "meeting_id")],
        inverseJoinColumns = [JoinColumn(name = "interest_id")],
    )
    var interests: MutableSet<Interest> = mutableSetOf(),

    @Column(name = "participants")
    @ManyToMany(targetEntity = User::class)
    @JoinTable(
        name = "participants_of_meeting",
        joinColumns = [JoinColumn(name = "meeting_id")],
        inverseJoinColumns = [JoinColumn(name = "user_id")],
    )
    val participants: MutableList<User> = mutableListOf(admin),

    @OneToOne(targetEntity = Chat::class, cascade = [CascadeType.ALL])
    var chat: Chat = Chat(),

) : FilteredByName, FilteredByInterests {

    @JsonIgnore
    @OneToMany(targetEntity = Image::class, mappedBy = "meeting")
    val images: MutableList<Image> = mutableListOf()

    val numberOfParticipants: Int = participants.size

    override val filteredInterests: List<String>
        get() = interests.map(Interest::name)

    override val filteredName: String
        get() = name
}
