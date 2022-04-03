package com.meetme.config

import com.meetme.chat.mapper.MessageToMessageDto
import com.meetme.chat.mapper.MessageToMessageDtoImpl
import com.meetme.group.mapper.GroupToGroupDto
import com.meetme.group.mapper.GroupToGroupDtoImpl
import com.meetme.interest.mapper.InterestsToStrings
import com.meetme.interest.mapper.InterestsToStringsImpl
import com.meetme.media_link.mapper.MediaLinksToMap
import com.meetme.media_link.mapper.MediaLinksToMapImpl
import com.meetme.meeting.mapper.MeetingToMeetingDto
import com.meetme.meeting.mapper.MeetingToMeetingDtoImpl
import com.meetme.user.mapper.UserToUserDto
import com.meetme.user.mapper.UserToUserDtoImpl
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