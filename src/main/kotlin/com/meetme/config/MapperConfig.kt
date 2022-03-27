package com.meetme.config

import com.meetme.mapper.InterestsToStrings
import com.meetme.mapper.InterestsToStringsImpl
import com.meetme.mapper.MeetingToMeetingDto
import com.meetme.mapper.MeetingToMeetingDtoImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MapperConfig {

    @Bean
    fun interestsToStrings(): InterestsToStrings =
        InterestsToStringsImpl()

    @Bean
    fun meetingToMeetingDto(): MeetingToMeetingDto =
        MeetingToMeetingDtoImpl(interestsToStrings())
}