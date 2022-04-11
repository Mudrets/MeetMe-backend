package com.meetme.config

import com.meetme.service.chat.mapper.MessageToMessageDto
import com.meetme.service.chat.mapper.MessageToMessageDtoImpl
import com.meetme.service.group.mapper.GroupToGroupDto
import com.meetme.service.group.mapper.GroupToGroupDtoImpl
import com.meetme.service.interest.mapper.InterestsToStrings
import com.meetme.service.interest.mapper.InterestsToStringsImpl
import com.meetme.service.media_link.mapper.MediaLinksToMap
import com.meetme.service.media_link.mapper.MediaLinksToMapImpl
import com.meetme.service.meeting.mapper.MeetingToMeetingDto
import com.meetme.service.meeting.mapper.MeetingToMeetingDtoImpl
import com.meetme.service.user.mapper.UserToUserDto
import com.meetme.service.user.mapper.UserToUserDtoImpl
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
        )

    @Bean
    fun messageToMessageDto(): MessageToMessageDto =
        MessageToMessageDtoImpl()
}