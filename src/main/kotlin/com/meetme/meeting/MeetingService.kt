package com.meetme.meeting

import com.meetme.StoreService
import com.meetme.domain.AllEntitiesGetter
import com.meetme.meeting.db.Meeting
import org.springframework.stereotype.Service

@Service
interface MeetingService : StoreService<Long, Meeting>, AllEntitiesGetter<Meeting> {
}