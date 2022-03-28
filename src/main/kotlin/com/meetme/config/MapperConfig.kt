package com.meetme.config

import com.meetme.mapper.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MapperConfig {

    @Bean
    fun interestsToStrings(): InterestsToStrings = InterestsToStringsImpl()

    @Bean
    fun mediaLinksToMap(): MediaLinksToMap = MediaLinksToMapImpl()

    @Bean
    fun meetingToMeetingDto(): MeetingToMeetingDto =
        MeetingToMeetingDtoImpl(interestsToStrings())

    @Bean
    fun userToUserDto(): UserToUserDto =
        UserToUserDtoImpl(
            interestsToStrings(),
            mediaLinksToMap(),
        )

    @Bean
    fun groupToGroupDto(): GroupToGroupDto =
        GroupToGroupDtoImpl(
            interestsToStrings(),
            mediaLinksToMap(),
        )
}