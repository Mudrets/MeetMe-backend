package com.meetme.service.interest

import com.meetme.db.interest.Interest
import com.meetme.db.interest.InterestDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class InterestService {

    @Autowired
    private lateinit var interestDao: InterestDao

    fun convertToInterestEntityAndAddNewInterests(interests: Set<String>): MutableSet<Interest> {
        val interestsSet = mutableSetOf<Interest>()
        for (interestName in interests) {
            val dbInterest = interestDao.findByName(interestName) ?: interestDao.save(Interest(name = interestName))
            interestsSet.add(dbInterest)
        }
        return interestsSet
    }
}