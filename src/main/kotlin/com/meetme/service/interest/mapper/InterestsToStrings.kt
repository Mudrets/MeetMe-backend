package com.meetme.service.interest.mapper

import com.meetme.db.interest.Interest

/**
 * Маппер, преобразующий Set<Interest> в List<String>.
 */
interface InterestsToStrings : (Set<Interest>) -> List<String>