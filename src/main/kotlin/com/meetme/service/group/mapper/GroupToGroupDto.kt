package com.meetme.service.group.mapper

import com.meetme.db.group.Group
import com.meetme.domain.dto.goup.GroupDto

/**
 * Маппер, преобразующий Group в GroupDto.
 */
interface GroupToGroupDto : (Group) -> GroupDto

