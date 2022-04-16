package com.meetme.config

import com.meetme.domain.dto.auth.UserDto
import com.meetme.domain.dto.goup.SearchGroupDto
import com.meetme.domain.dto.meeting.SearchMeetingDto
import com.meetme.domain.dto.meeting.enums.MeetingType
import com.meetme.domain.filter.*
import com.meetme.db.group.Group
import com.meetme.service.interest.mapper.InterestsToStrings
import com.meetme.db.meeting.Meeting
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.text.SimpleDateFormat

/**
 * Класс предоставляющий зависимости для фильтрации
 * во время поиска.
 */
@Configuration
class FilterConfig {

    /**
     * Фильтр по имени.
     * @return Возвращает фильтер для реализации поиска по имени.
     */
    @Bean
    fun nameFilter(): Filter<String, String> = StringFilter()


    /**
     * Фильтр по интересам.
     * @return Возвращает фильтр доя реализации посика по интересам.
     */
    @Bean
    fun interestsFilter(): Filter<Collection<*>, Collection<*>> = CollectionFilter()

    /**
     * Фильтр по типу мепроприятия.
     * @return Возращает фильтр для реализации поиска по типу мероприятия.
     */
    @Bean
    fun typeFilter() = object : Filter<Boolean, MeetingType> {
        override fun invoke(isVisited: Boolean, type: MeetingType): Boolean =
            (isVisited && type == MeetingType.VISITED) ||
                (!isVisited && type == MeetingType.PLANNED)
    }

    /**
     * Фильтр по дате окончания.
     * @return Возвращает фильтр для реализации поиска по дате.
     */
    @Qualifier("endDateFilter")
    @Bean
    fun endDateFilter() = object : Filter<Meeting, String?> {
        override fun invoke(meeting: Meeting, filterEndDate: String?): Boolean {
            if (filterEndDate == null)
                return true
            val endDateStr = meeting.endDate ?: meeting.startDate
            val format = SimpleDateFormat("MM-dd-yyyy HH:mm")
            val endDate = format.parse(endDateStr)
            val filterDate = format.parse(filterEndDate)
            return filterDate.before(endDate)
        }
    }

    /**
     * Фильтр по дате начала.
     * @return Возвращает фильтр для реализации поиска по дате.
     */
    @Qualifier("startDateFilter")
    @Bean
    fun startDateFilter() = object : Filter<Meeting, String?> {
        override fun invoke(meeting: Meeting, filterStartDate: String?): Boolean {
            if (filterStartDate == null)
                return true
            val startDateStr = meeting.startDate
            val format = SimpleDateFormat("MM-dd-yyyy HH:mm")
            val startDate = format.parse(startDateStr)
            val filterDate = format.parse(filterStartDate)
            return filterDate.after(startDate)
        }
    }

    /**
     * Фильтр по дате окончания.
     * @return Возвращает фильтр для реализации поиска по дате.
     */
    @Bean
    fun maxNumberOfParticipantsFilter() = object : Filter<Int, Int?> {
        override fun invoke(maxNumberParticipants: Int, filterNumber: Int?): Boolean =
            filterNumber == null || maxNumberParticipants <= filterNumber
    }

    /**
     * Фильтр мероприятий.
     * @param nameFilter фильтр по имени;
     * @param interestFilter фильтр по интересам;
     * @param typeFilter фильтр по типу мероприятия;
     * @param startDateFilter фильтр по дате начала мероприятия;
     * @param endDateFilter фильтр по дате конца мероприятия;
     * @param maxNumberOfParticipantsFilter фильтр по максимальному количеству человек на мероприятии;
     * @param interestsToString маппер интеревсов в список строк;
     * @return Возвращает фильтр для реализации посика мероприятий по переданным параметрам.
     */
    @Bean
    fun meetingFilter(
        @Autowired nameFilter: Filter<String, String>,
        @Autowired interestFilter: Filter<Collection<*>, Collection<*>>,
        @Autowired typeFilter: Filter<Boolean, MeetingType>,
        @Qualifier("startDateFilter") @Autowired startDateFilter: Filter<Meeting, String?>,
        @Qualifier("endDateFilter") @Autowired endDateFilter: Filter<Meeting, String?>,
        @Autowired maxNumberOfParticipantsFilter: Filter<Int, Int?>,
        @Autowired interestsToString: InterestsToStrings,
    ) = object : Filter<Meeting, SearchMeetingDto> {
        override fun invoke(meeting: Meeting, searchMeetingDto: SearchMeetingDto): Boolean {
            val stringInterestList = interestsToString(meeting.interests)

            return nameFilter(meeting.name, searchMeetingDto.searchQuery) &&
                interestFilter(stringInterestList, searchMeetingDto.interests) &&
                typeFilter(meeting.isVisitedMeeting, searchMeetingDto.type) &&
                startDateFilter(meeting, searchMeetingDto.startDate) &&
                maxNumberOfParticipantsFilter(meeting.maxNumberOfParticipants, searchMeetingDto.maxNumberOfParticipants)
        }
    }

    /**
     * Фильтр групп.
     * @param nameFilter фильтр по названию группы;
     * @param interestFilter фильтр по интересам;
     * @param interestsToString маппер интеревсов в список строк;
     * @return Возвращает фильтр для реализации поиска групп по переданным параметрам.
     */
    @Bean
    fun groupFilter(
        @Autowired nameFilter: Filter<String, String>,
        @Autowired interestFilter: Filter<Collection<*>, Collection<*>>,
        @Autowired interestsToString: InterestsToStrings,
    ) = object : Filter<Group, SearchGroupDto> {
        override fun invoke(group: Group, searchGroupDto: SearchGroupDto): Boolean {
            val stringInterestList = interestsToString(group.interests)

            return nameFilter(group.name, searchGroupDto.searchQuery) &&
                interestFilter(stringInterestList, searchGroupDto.interests)
        }
    }

    /**
     * Фильтр пользователей.
     * @param nameFilter фильтр по имени;
     * @return Возвращает фильтр для реализации поиска пользователей по имени.
     */
    @Bean
    fun userFilter(
        @Autowired nameFilter: Filter<String, String>,
    ) = object : Filter<UserDto, String> {
        override fun invoke(user: UserDto, query: String): Boolean {
            return nameFilter(user.fullName, query)
        }
    }
}