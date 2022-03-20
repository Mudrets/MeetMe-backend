package com.meetme.group.post

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository("postRepository")
interface PostDao : JpaRepository<Post, Long>