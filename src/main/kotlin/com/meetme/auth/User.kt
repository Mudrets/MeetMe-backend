package com.meetme.auth

import com.fasterxml.jackson.annotation.JsonIgnore
import com.meetme.group.Group
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

    @Column(unique = true)
    var email: String? = null,

    @Column(unique = true)
    var telephone: String? = null,

    @JvmField
    var password: String? = null,

    @JsonIgnore
    @OneToMany(mappedBy = "admin")
    var managedMeetings: Set<Meeting> = setOf(),

    @JsonIgnore
    @ManyToMany(
        targetEntity = Meeting::class,
        mappedBy = "participants"
    )
    var meetings: Set<Meeting> = setOf(),

    @JsonIgnore
    @OneToMany(mappedBy = "admin")
    var managedGroup: Set<Group> = setOf(),

    @JsonIgnore
    @ManyToMany(
        targetEntity = Group::class,
        mappedBy = "participants"
    )
    var groups: Set<Group> = setOf()

) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority>? = null

    override fun getPassword(): String? = password

    override fun getUsername(): String? = email

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true

}