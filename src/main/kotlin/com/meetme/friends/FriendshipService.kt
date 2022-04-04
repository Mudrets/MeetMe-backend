package com.meetme.friends

import com.meetme.friends.enums.FriendshipStatus
import com.meetme.user.db.User

interface FriendshipService {

    fun getFriendRequestToUser(userId: Long): List<User>

    fun getFriendRequestFromUser(userId: Long): List<User>

    fun getFriendsOfUser(userId: Long): List<User>

    fun getAllPeopleWithFriends(userId: Long): Map<FriendshipStatus, List<User>>

    fun sendRequestFrom1To2(userId1: Long, userId2: Long)

    fun removeRequestFrom1(userId1: Long, userId2: Long)
}