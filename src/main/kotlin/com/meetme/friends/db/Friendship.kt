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
    val user1: User = User(),

    @ManyToOne(targetEntity = User::class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user2_id")
    val user2: User = User(),

    @Column(name = "accept_from_user1")
    var acceptFromUser1: Boolean = false,

    @Column(name = "accept_from_user2")
    var acceptFromUser2: Boolean = false,
) {
    val isFriendship: Boolean
        get() = acceptFromUser1 && acceptFromUser2

    val canceledFriendship: Boolean
        get() = !acceptFromUser1 && !acceptFromUser2

    fun isFriendRequestToUser(user: User): Boolean =
        user1 == user && acceptFromUser2 && !acceptFromUser1 ||
            user2 == user && acceptFromUser1 && !acceptFromUser2

    fun isFriendRequestFromUser(user: User): Boolean =
        user1 == user && acceptFromUser1 && !acceptFromUser2 ||
            user2 == user && acceptFromUser2 && !acceptFromUser1

    fun setAcceptFrom(user: User) {
        if (user1 == user)
            acceptFromUser1 = true
        else if (user2 == user)
            acceptFromUser2 = true
    }

    fun cancelAcceptFor(user: User) {
        if (user1 == user)
            acceptFromUser1 = false
        else if (user2 == user)
            acceptFromUser2 = false
    }

    fun getFriend(user: User) =
        if (user1 == user) user2 else user1
}