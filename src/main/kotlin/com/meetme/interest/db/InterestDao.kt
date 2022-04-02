package com.meetme.interest.db

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository("interestRepository")
interface InterestDao : JpaRepository<Interest, Long> {
    fun findByName(name: String): Interest?
}