package com.meetme.service.interest

import com.meetme.db.interest.Interest

/**
 * Сервис для работы с интересами.
 */
interface InterestService {

    /**
     * Преобразует Set<String>, представляющий список названий интеревсов в
     * MutableSet<Interest>.
     * @param interests сет названий интересов.
     * @return Возвращает сет созданного или полеченного из хранилища сета интересов.
     */
    fun convertToInterestEntityAndAddNewInterests(interests: Set<String>): MutableSet<Interest>

}