package com.meetme.service.group

import com.meetme.domain.CrudService
import com.meetme.domain.dto.goup.CreateGroupDto
import com.meetme.domain.dto.goup.UpdateGroupDto
import com.meetme.db.group.Group
import com.meetme.db.meeting.Meeting
import org.springframework.stereotype.Service

/**
 * Сервис для работы с группами.
 */
@Service
interface GroupService : CrudService<CreateGroupDto, UpdateGroupDto, Long, Group> {
    /**
     * Получает список мероприятий в которых группа принимает участие.
     * @param groupId идентификатор группы.
     * @return Возвращает список мероприятий, в которых группа принимает участие.
     */
    fun getMeetings(groupId: Long): List<Meeting>
}