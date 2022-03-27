package com.meetme.mapper

import com.meetme.iterest.Interest
import org.springframework.context.annotation.Bean

interface InterestsToStrings : (List<Interest>) -> List<String>

class InterestsToStringsImpl : InterestsToStrings {
    override fun invoke(interests: List<Interest>): List<String> =
        interests.map { interest -> interest.name }

}