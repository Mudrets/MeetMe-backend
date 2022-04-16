package com.meetme.service.interest

import com.meetme.db.interest.Interest
import com.meetme.db.interest.InterestDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Сервис для работы с интересами.
 */
@Service
class InterestServiceImpl @Autowired constructor(
    /**
     * Data access object для работы с данными в таблице интересов в базе данных.
     */
    private val interestDao: InterestDao
) : InterestService {
    /**
     * Преобразует Set<String>, представляющий список названий интеревсов в
     * MutableSet<Interest>.
     * @param interests сет названий интересов.
     * @return Возвращает сет созданного или полеченного из хранилища сета интересов.
     */
    override fun convertToInterestEntityAndAddNewInterests(interests: Set<String>): MutableSet<Interest> {
        val interestsSet = mutableSetOf<Interest>()
        for (interestName in interests) {
            val dbInterest = interestDao.findByName(interestName) ?: interestDao.save(Interest(name = interestName))
            interestsSet.add(dbInterest)
        }
        return interestsSet
    }
}