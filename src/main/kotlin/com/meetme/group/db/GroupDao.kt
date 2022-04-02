package com.meetme.group.db

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository("groupRepository")
interface GroupDao : JpaRepository<Group, Long> {
    fun findAllByPrivate(isPrivate: Boolean): List<Group>
}