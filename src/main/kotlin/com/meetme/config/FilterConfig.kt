package com.meetme.config

import com.meetme.domain.dto.auth.UserDto
import com.meetme.domain.dto.goup.SearchGroupDto
import com.meetme.domain.dto.meeting.SearchMeetingDto
import com.meetme.domain.dto.meeting.enums.MeetingType
import com.meetme.domain.filter.*
import com.meetme.group.db.Group
import com.meetme.interest.mapper.InterestsToStrings
import com.meetme.meeting.db.Meeting
import com.meetme.user.db.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FilterConfig {

    @Bean
    fun nameFilter(): Filter<String, String> = StringFilter()

    @Bean
    fun interestsFilter(): Filter<Collection<*>, Collection<*>> = CollectionFilter()

    @Bean
    fun typeFilter() = object : Filter<Boolean, MeetingType> {
        override fun invoke(isVisited: Boolean, type: MeetingType): Boolean =
            (isVisited && type == MeetingType.VISITED) ||
                (!isVisited && type == MeetingType.PLANNED)
    }

    @Bean
    fun meetingFilter(
        @Autowired nameFilter: Filter<String, String>,
        @Autowired interestFilter: Filter<Collection<*>, Collection<*>>,
        @Autowired typeFilter: Filter<Boolean, MeetingType>,
        @Autowired interestsToString: InterestsToStrings,
    ) = object : Filter<Meeting, SearchMeetingDto> {
        override fun invoke(meeting: Meeting, searchMeetingDto: SearchMeetingDto): Boolean {
            val stringInterestList = interestsToString(meeting.interests)

            return nameFilter(meeting.name, searchMeetingDto.searchQuery) &&
                interestFilter(stringInterestList, searchMeetingDto.interests) &&
                typeFilter(meeting.isVisitedMeeting, searchMeetingDto.type)
        }
    }
    
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

    @Bean
    fun userFilter(
        @Autowired nameFilter: Filter<String, String>,
    ) = object : Filter<UserDto, String> {
        override fun invoke(user: UserDto, query: String): Boolean {
            return nameFilter(user.fullName, query)
        }
    }
}