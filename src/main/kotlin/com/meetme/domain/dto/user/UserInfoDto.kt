package com.meetme.domain.dto.user

data class UserInfoDto(
    val id: Long,
    private val name: String,
    private val surname: String,
    val photoUrl: String? = null,
) {
    val fullName = "$name $surname"
}