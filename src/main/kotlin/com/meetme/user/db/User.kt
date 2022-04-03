package com.meetme.user.db

import com.fasterxml.jackson.annotation.JsonIgnore
import com.meetme.media_link.db.MediaLink
import com.meetme.domain.filter.entity.FilteredByName
import com.meetme.group.db.Group
import com.meetme.interest.db.Interest
import com.meetme.invitation.db.Invitation
import com.meetme.meeting.db.Meeting
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.*

@Entity(name = "users")
data class User(
    @Id
    @SequenceGenerator(
        name = "user_sequence",
        sequenceName = "user_sequence",
        allocationSize = 1,
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "user_sequence"
    )
    @Column(name = "user_id")
    val id: Long = 0,

    @Column(name = "name")
    var name: String = "",

    @Column(name = "surname")
    var surname: String = "",

    @Column(unique = true)
    var email: String? = null,

    @Column(unique = true)
    var telephone: String? = null,

    @JvmField
    var password: String? = null,

    @Column(name = "description")
    var description: String? = null,

    @Column(name = "photo_url")
    var photoUrl: String = "",


) : UserDetails, FilteredByName {

    @JsonIgnore
    @OneToMany(mappedBy = "admin", cascade = [CascadeType.REMOVE])
    var managedMeetings: MutableSet<Meeting> = mutableSetOf()

    @JsonIgnore
    @ManyToMany(
        targetEntity = Meeting::class,
        cascade = [CascadeType.PERSIST, CascadeType.MERGE],
        mappedBy = "participants"
    )
    var meetings: MutableSet<Meeting> = mutableSetOf()

    @JsonIgnore
    @OneToMany(mappedBy = "admin", cascade = [CascadeType.REMOVE])
    var managedGroup: MutableSet<Group> = mutableSetOf()

    @JsonIgnore
    @ManyToMany(
        targetEntity = Group::class,
        cascade = [CascadeType.PERSIST, CascadeType.MERGE],
        mappedBy = "participants"
    )
    var groups: MutableSet<Group> = mutableSetOf()

    @ManyToMany(targetEntity = Interest::class)
    var interests: Set<Interest> = mutableSetOf()

    @OneToMany(targetEntity = MediaLink::class, mappedBy = "user")
    var socialMediaLinks: Set<MediaLink> = mutableSetOf()

    @ManyToMany(targetEntity = Invitation::class, mappedBy = "users")
    val invitations: MutableList<Invitation> = mutableListOf()

    val allMeetings: Set<Meeting>
        get() = meetings.union(managedMeetings)

    override fun getAuthorities(): MutableCollection<out GrantedAuthority>? = null

    override fun getPassword(): String? = password

    override fun getUsername(): String? = email

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true

    val fullName: String
        get() = "$name${if (surname.isNotBlank()) " $surname" else ""}"

    override val filteredName: String
        get() = fullName
}