package com.meetme.domain.dto

/**
 * Структура ответа сервера на клиентский запрос.
 */
data class DataResponse<out T>(
    /**
     * Сообщение.
     */
    val message: String = "success",
    /**
     * Код для отображения уведобления на клиентском
     * приложении.
     */
    val appCode: Int = 0,
    /**
     * Данные ответа сервера.
     */
    val data: T? = null,
)
