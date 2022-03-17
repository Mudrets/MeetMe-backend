package com.meetme.dto.friends

data class FriendDto(
    val id: Long,
    private val name: String,
    private val surname: String,
    val photoUrl: String? = null,
) {
    val fullName = "$name $surname"
}