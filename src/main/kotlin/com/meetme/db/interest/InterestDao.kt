package com.meetme.db.interest

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Интерфейс предоставляющий абстракцию для работы с таблицей интересов в базе данных.
 */
@Repository("interestRepository")
interface InterestDao : JpaRepository<Interest, Long> {
    /**
     * Ищет интерес по переданному названию.
     * @param name название искомого интереса.
     * @return Возвращает Interest с переданным именем или null
     * если такой интерес не был найден.
     */
    fun findByName(name: String): Interest?
}