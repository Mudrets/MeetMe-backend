package com.meetme.group

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository("groupRepository")
interface GroupDao : JpaRepository<Group, Long>