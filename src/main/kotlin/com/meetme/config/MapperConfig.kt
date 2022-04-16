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

/**
 * Предоставляет мапперы в качестве зависимостей.
 */
@Configuration
class MapperConfig {

    /**
     * Предоставляет маппер преобразующий список интересов в список строк с названиями интересов.
     * @return Возвращает маппер преобразующий List<Interest> в List<String>.
     */
    @Bean
    fun interestsToStrings(): InterestsToStrings = InterestsToStringsImpl()

    /**
     * Предоставляет маппер преобразующий список List<MediaLinks> в Map<String, String>.
     * @return Возвращает маппер преобразующий MediaLink в Map<String, String>, где ключ - это название ссылки,
     * а знаение - это сама ссылка.
     */
    @Bean
    fun mediaLinksToMap(): MediaLinksToMap = MediaLinksToMapImpl()

    /**
     * Предоставляет маппер преобразующий Meeting в MeetingDto.
     * @return Возвращает маппер преобразующий Meeting в MeetingDto хранящий информацию о мероприятии необходимую
     * клиентскому приложению.
     */
    @Bean
    fun meetingToMeetingDto(): MeetingToMeetingDto =
        MeetingToMeetingDtoImpl(interestsToStrings())

    /**
     * Предоставляет маппер преобразующий User в UserDto.
     * @return Возвращает маппер преобразующий User в UserDto хранящий информацию о пользователе необходимую
     * клиентскому приложению.
     */
    @Bean
    fun userToUserDto(): UserToUserDto =
        UserToUserDtoImpl(
            interestsToStrings(),
            mediaLinksToMap(),
        )

    /**
     * Предоставляет маппер преобразующий Group в GroupDto.
     * @return Возвращает маппер преобразующий Group в GroupDto хранящий информацию о группе необходимую
     * клиентскому приложению.
     */
    @Bean
    fun groupToGroupDto(): GroupToGroupDto =
        GroupToGroupDtoImpl(
            interestsToStrings(),
        )

    /**
     * Предоставляет маппер преобразующий Message в MessageDto.
     * @return Возвращает маппер преобразующий Message в MessageDto хранящий информацию о сообщении,
     * необходимую клиентскому приложению.
     */
    @Bean
    fun messageToMessageDto(): MessageToMessageDto =
        MessageToMessageDtoImpl()
}