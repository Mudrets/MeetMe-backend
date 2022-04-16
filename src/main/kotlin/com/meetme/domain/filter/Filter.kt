package com.meetme.domain.filter

/**
 * Базовый интерфейс фильтра
 */
interface Filter<T, M> : (T, M) -> Boolean