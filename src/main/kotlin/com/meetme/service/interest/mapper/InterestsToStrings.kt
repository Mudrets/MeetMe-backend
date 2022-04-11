package com.meetme.service.interest.mapper

import com.meetme.db.interest.Interest

interface InterestsToStrings : (Set<Interest>) -> List<String>

class InterestsToStringsImpl : InterestsToStrings {
    override fun invoke(interests: Set<Interest>): List<String> =
        interests.map { interest -> interest.name }

}