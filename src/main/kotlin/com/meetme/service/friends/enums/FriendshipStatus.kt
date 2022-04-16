package com.meetme.service.friends.enums

/**
 * Статус дружеских отношений.
 */
enum class FriendshipStatus(val status: String) {
    FRIEND("friends"), NOT_FRIEND("global")
}