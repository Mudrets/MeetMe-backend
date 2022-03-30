package com.meetme.mapper

import com.meetme.services.iterest.Interest
import org.springframework.context.annotation.Bean

interface InterestsToStrings : (Set<Interest>) -> List<String>

class InterestsToStringsImpl : InterestsToStrings {
    override fun invoke(interests: Set<Interest>): List<String> =
        interests.map { interest -> interest.name }

}