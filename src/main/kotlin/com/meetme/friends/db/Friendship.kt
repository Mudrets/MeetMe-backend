package com.meetme.friends.db

import com.meetme.user.db.User
import javax.persistence.*

@Entity
class Friendship(
    @Id
    @SequenceGenerator(
        name = "friendship_sequence",
        sequenceName = "friendship_sequence",
        allocationSize = 1,
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "friendship_sequence"
    )
    @Column(name = "friendship_id")
    val id: Long = 0,

    @ManyToOne(targetEntity = User::class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user1_id")
    val user1: User? = null,

    @ManyToOne(targetEntity = User::class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user2_id")
    val user2: User? = null,

    @Column(name = "accept_from_user1")
    var acceptFromUser1: Boolean = false,

    @Column(name = "accept_from_user2")
    var acceptFromUser2: Boolean = false,
)