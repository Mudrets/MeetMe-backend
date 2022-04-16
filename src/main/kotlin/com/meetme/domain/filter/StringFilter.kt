package com.meetme.domain.filter

/**
 * Фильтр строки по строке. Проверяет содержится ли
 * искомая строка в исходной строке.
 */
class StringFilter : Filter<String, String> {
    override fun invoke(name: String, query: String): Boolean =
        name.contains(query, true)
}