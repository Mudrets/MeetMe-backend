package com.meetme.friends

import com.meetme.doIfExist
import com.meetme.user.db.User
import com.meetme.friends.db.Friendship
import com.meetme.friends.db.FriendshipDao
import com.meetme.friends.enums.FriendshipStatus
import com.meetme.user.UserServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class FriendshipServiceImpl : FriendshipService {

    @Autowired
    private lateinit var friendshipDao: FriendshipDao

    @Autowired
    private lateinit var userService: UserServiceImpl

    private fun getFriendship(user1: User, user2: User): Friendship? =
        friendshipDao.findByUser1AndUser2(user1, user2)
            ?: friendshipDao.findByUser1AndUser2(user2, user1)

    private fun getFriendshipForRemove(user1: User, user2: User) =
        getFriendship(user1, user2)
            ?: throw IllegalArgumentException(
                "Friendship between user with id = ${user1.id} and user with id = ${user2.id} does not exist"
            )

    private fun getFriendsFilterBy(user: User, filter: (Friendship) -> Boolean): List<User> =
        friendshipDao.findAllByUser1OrUser2(user, user).asSequence()
            .filter(filter)
            .map { friendship -> friendship.getFriend(user) }
            .toList()

    private fun createFriendship(user1: User, user2: User) =
        friendshipDao.save(
            Friendship(
                user1 = user1,
                user2 = user2,
                acceptFromUser1 = true
            )
        )

    private fun getAllFriends(user: User): List<User> =
        friendshipDao.findAllByUser1OrUser2(user, user)
            .filter(Friendship::isFriendship)
            .map { friendship -> friendship.getFriend(user) }

    override fun getFriendRequestToUser(userId: Long): List<User> =
        userId.doIfExist(userService) { user ->
            getFriendsFilterBy(user) { friendship -> friendship.isFriendRequestToUser(user) }
        }

    override fun getFriendRequestFromUser(userId: Long): List<User> =
        userId.doIfExist(userService) { user ->
            getFriendsFilterBy(user) { friendship -> friendship.isFriendRequestFromUser(user) }
        }

    override fun getFriendsOfUser(userId: Long): List<User> =
        userId.doIfExist(userService) { user -> getAllFriends(user) }

    override fun getAllPeopleWithFriends(userId: Long): Map<FriendshipStatus, List<User>> =
        userId.doIfExist(userService) { user ->
            val allUsers = userService.getAll().subtract(listOf(user).toSet())
            val friends = getAllFriends(user)
            val resMap = mapOf(
                FriendshipStatus.Friend to mutableListOf<User>(),
                FriendshipStatus.NotFriend to mutableListOf<User>()
            )
            allUsers.forEach { globalUser ->
                    if (friends.contains(globalUser))
                        resMap[FriendshipStatus.Friend]?.add(globalUser)
                    else
                        resMap[FriendshipStatus.NotFriend]?.add(globalUser)
                }
            resMap
        }

    override fun sendRequestFrom1To2(userId1: Long, userId2: Long) {
        (userId1 to userId2).doIfExist(userService) { user1, user2 ->
            val friendship = getFriendship(user1, user2)
            if (friendship == null) {
                createFriendship(user1, user2)
            } else {
                friendship.setAcceptFrom(user1)
                friendshipDao.save(friendship)
            }
        }
    }

    override fun removeRequestFrom1(userId1: Long, userId2: Long) {
        (userId1 to userId2).doIfExist(userService) { user1, user2 ->
            val friendship = getFriendshipForRemove(user1, user2)
            friendship.cancelAcceptFor(user1)
            if (friendship.canceledFriendship)
                friendshipDao.delete(friendship)
            else
                friendshipDao.save(friendship)
        }
    }

}