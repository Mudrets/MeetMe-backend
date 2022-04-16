package com.meetme.service.interest.mapper

import com.meetme.db.interest.Interest

/**
 * Реализация маппера, преобразующий Set<Interest> в List<String>.
 */
class InterestsToStringsImpl : InterestsToStrings {
    override fun invoke(interests: Set<Interest>): List<String> =
        interests.map { interest -> interest.name }

}