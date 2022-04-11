package com.meetme.service.participants.group

import com.meetme.service.group.GroupService
import com.meetme.db.group.Group
import com.meetme.service.participants.base.ParticipantsBaseService
import com.meetme.service.user.UserService
import com.meetme.db.user.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service("groupParticipantsService")
class GroupParticipantsService @Autowired constructor(
    userService: UserService,
    groupService: GroupService,
) : ParticipantsBaseService<Group>(userService, groupService) {

    override fun checkEntityBeforeAdd(entity: Group, user: User) {
        if (entity.participants.contains(user))
            throw IllegalArgumentException(
                "User with id = ${user.id} already is participant of meeting with id = ${entity.id}"
            )
    }

    override fun checkEntityBeforeRemove(entity: Group, user: User) {
        if (!entity.participants.contains(user))
            throw IllegalArgumentException(
                "The user with id = ${user.id} is not a member of the meeting ${entity.id}"
            )
    }

    override fun addContainerToUser(container: Group, user: User) {
        user.groups.add(container)
    }

    override fun removeContainerFromUser(container: Group, user: User) {
        user.groups.remove(container)
    }
}