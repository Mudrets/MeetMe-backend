package com.meetme.friends

import com.meetme.auth.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class FriendshipService {

    @Autowired
    private lateinit var friendshipDao: FriendshipDao

    private fun checkUsers(user1: User, user2: User) {
        if (user1 == user2)
            throw IllegalArgumentException("user1: $user1 and user2: $user2 are equal")
    }

    private fun getFriendship(user: User, friend: User) =
        friendshipDao.findByUser1AndUser2(user, friend)
            ?: friendshipDao.findByUser1AndUser2(friend, user)

    private fun checkForAdd(user: User, friend: User): Friendship? {
        val friendship = getFriendship(user, friend)
        if (friendship?.user1 == user && friendship.acceptFromUser1 ||
            friendship?.user2 == user && friendship.acceptFromUser2)
            throw IllegalArgumentException("user1: $user have user2: $friend as a friend")

        return friendship
    }

    private fun checkForRemove(user: User, friend: User): Friendship {
        val friendship = getFriendship(user, friend)
        if (friendship?.user1 != user && friendship?.acceptFromUser2 != true ||
            friendship.user2 != user && !friendship.acceptFromUser1)
            throw IllegalArgumentException("user1: $user do not have user2: $friend as a friend")

        return friendship
    }

    private fun getFriendsFilterBy(user: User, filter: (Friendship) -> Boolean): List<User> =
        friendshipDao.findAllByUser1OrUser2(user, user)?.asSequence()
            ?.filter(filter)
            ?.map { friendship ->
                if (friendship.user1 == user)
                    friendship.user2!!
                else
                    friendship.user1!!
            }
            ?.sortedBy(User::fullname)
            ?.toList() ?: listOf()

    fun getFriendRequestToUser(user: User): List<User> =
        getFriendsFilterBy(user) { friendship ->
            friendship.user1 == user && friendship.acceptFromUser2 && !friendship.acceptFromUser1 ||
                friendship.user2 == user && friendship.acceptFromUser1 && !friendship.acceptFromUser2
        }

    fun getFriendRequestFromUser(user: User): List<User> =
        getFriendsFilterBy(user) { friendship ->
            friendship.user1 == user && friendship.acceptFromUser1 && !friendship.acceptFromUser2 ||
                friendship.user2 == user && friendship.acceptFromUser2 && !friendship.acceptFromUser1
        }

    fun getFriendsOfUser(user: User): List<User> =
        getFriendsFilterBy(user) { friendship ->
            friendship.acceptFromUser2 && friendship.acceptFromUser1
        }

    @Throws(IllegalArgumentException::class)
    fun createNewFriendship(user: User, friend: User): Friendship {
        checkUsers(user, friend)
        val friendship = checkForAdd(user, friend)

        return if (friendship == null) {
            friendshipDao.save(
                Friendship(
                    user1 = user,
                    user2 = friend,
                    acceptFromUser1 = true
                )
            )
        } else {
            if (friendship.user1 == user)
                friendship.acceptFromUser1 = true
            else
                friendship.acceptFromUser2 = true

            friendshipDao.save(friendship)
        }
    }

    @Throws(IllegalArgumentException::class)
    fun removeFriendShip(user: User, friend: User) {
        checkUsers(user, friend)
        val friendship = checkForRemove(user, friend)

        if (friendship.user1 == user && friendship.acceptFromUser2) {
            friendship.acceptFromUser1 = false
            friendshipDao.save(friendship)
        } else if (friendship.user2 == user && friendship.acceptFromUser1) {
            friendship.acceptFromUser2 = false
            friendshipDao.save(friendship)
        } else {
            friendshipDao.delete(friendship)
        }
    }
}