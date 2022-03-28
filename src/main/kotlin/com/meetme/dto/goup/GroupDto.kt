package com.meetme.dto.goup

import com.meetme.auth.User
import com.meetme.group.post.Post
import com.meetme.iterest.Interest
import com.meetme.medialink.MediaLink

data class GroupDto(
    val id: Long,
    val adminId: Long,
    val name: String,
    val description: String,
    val photoUrl: String?,
    val isPrivate: Boolean,
    val posts: MutableList<Post> = mutableListOf(),
    val interests: List<String> = listOf(),
    val socialMediaLinks: Map<String, String> = mapOf()
)