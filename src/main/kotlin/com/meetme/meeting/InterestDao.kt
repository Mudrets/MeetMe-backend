package com.meetme.meeting

import org.springframework.data.jpa.repository.JpaRepository

interface InterestDao : JpaRepository<Interest, Long> {
    fun findByName(name: String): Interest?
}