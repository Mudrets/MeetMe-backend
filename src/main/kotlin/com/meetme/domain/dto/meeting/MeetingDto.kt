package com.meetme.domain.dto.meeting

/**
 * Data transfer object с информацией о мероприятии, необходимой клиентскому приложению.
 */
data class MeetingDto(
    /**
     * Идентификатор.
     */
    val id: Long,
    /**
     * Идентификатор админимтратора.
     */
    val adminId: Long?,
    /**
     * Название мероприятия.
     */
    val name: String,
    /**
     * Описание мероприятия.
     */
    val description: String? = null,
    /**
     * Дата начала проведения мероприятия.
     */
    val startDate: String,
    /**
     * Дата окончания проведения мероприятия.
     */
    val endDate: String? = null,
    /**
     * Является ли мероприятияе приватным.
     */
    val isPrivate: Boolean = false,
    /**
     * Проводится ли мероприятие в онлайн формате.
     */
    val isOnline: Boolean = true,
    /**
     * Место проведения мероприятия.
     */
    val location: String? = null,
    /**
     * Максимаьное количество участников мероприятия.
     */
    val maxNumberOfParticipants: Int = 1,
    /**
     * Количество участников мероприятия.
     */
    val numberOfParticipants: Int = 1,
    /**
     * Интересы меропритяия.
     */
    val interests: List<String> = listOf(),
    /**
     * Ссылка на фотографию мероприятия.
     */
    val imageUrl: String? = null,
    /**
     * Ялвяется ли пользователь участником этого меропритяия.
     */
    val isParticipant: Boolean? = null,
    /**
     * Идентификатор чата.
     */
    val chatId: Long = 0,
)