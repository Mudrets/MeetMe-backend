package com.meetme.interest.mapper

import com.meetme.interest.db.Interest

interface InterestsToStrings : (Set<Interest>) -> List<String>

class InterestsToStringsImpl : InterestsToStrings {
    override fun invoke(interests: Set<Interest>): List<String> =
        interests.map { interest -> interest.name }

}