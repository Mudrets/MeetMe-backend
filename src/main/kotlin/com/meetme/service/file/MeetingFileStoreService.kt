package com.meetme.service.file

import com.meetme.util.doIfExist
import com.meetme.service.meeting.MeetingService
import com.meetme.util.Constants.MEETING_DIR_NAME
import com.meetme.util.Constants.MEETING_IMAGE_PATH
import com.meetme.util.Constants.SERVER_ROOT
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Path

@Service("meetingFileStoreService")
class MeetingFileStoreService : BaseFileStoreService<Long>(
    pathOfStore = Path.of(MEETING_IMAGE_PATH),
    entityOfStorageName = "Meeting",
    rootImageUrl = "${SERVER_ROOT}/${MEETING_DIR_NAME}"
) {

    @Autowired
    private lateinit var meetingService: MeetingService

    override fun store(file: MultipartFile, id: Long): String =
        id.doIfExist(meetingService) { meeting ->
            val photoUrl = super.store(file, id)
            meeting.photoUrl = photoUrl
            meetingService.save(meeting)
            photoUrl
        }
}