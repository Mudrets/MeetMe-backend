package com.meetme.auth

import com.fasterxml.jackson.annotation.JsonIgnore
import com.meetme.group.Group
import com.meetme.iterest.Interest
import com.meetme.medialink.MediaLink
import com.meetme.meeting.Meeting
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
    var photoUrl: String? = null,


) : UserDetails {

    @JsonIgnore
    @OneToMany(mappedBy = "admin")
    var managedMeetings: MutableSet<Meeting> = mutableSetOf()

    @JsonIgnore
    @ManyToMany(
        targetEntity = Meeting::class,
        mappedBy = "participants"
    )
    var meetings: MutableSet<Meeting> = mutableSetOf()

    @JsonIgnore
    @OneToMany(mappedBy = "admin")
    var managedGroup: MutableSet<Group> = mutableSetOf()

    @JsonIgnore
    @ManyToMany(
        targetEntity = Group::class,
        mappedBy = "participants"
    )
    var groups: MutableSet<Group> = mutableSetOf()

    @ManyToMany(targetEntity = Interest::class)
    @JoinTable(
        name = "interests_of_user",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "interest_id")],
    )
    var interests: Set<Interest> = mutableSetOf()

    @OneToMany(targetEntity = MediaLink::class, mappedBy = "user")
    var socialMediaLinks: Set<MediaLink> = mutableSetOf()

    override fun getAuthorities(): MutableCollection<out GrantedAuthority>? = null

    override fun getPassword(): String? = password

    override fun getUsername(): String? = email

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true

    val fullname = "$name $surname"

    override fun toString(): String {
        return "User(" +
            "id: $id, " +
            "name: $name, " +
            "surname: $surname, " +
            ")"
    }
}