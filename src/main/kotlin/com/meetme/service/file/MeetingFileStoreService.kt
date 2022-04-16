package com.meetme.service.file

import com.meetme.util.doIfExist
import com.meetme.service.meeting.MeetingService
import com.meetme.util.Constants.MEETING_DIR_NAME
import com.meetme.util.Constants.MEETING_IMAGE_PATH
import com.meetme.util.Constants.SERVER_IMAGE_ROOT
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Path

/**
 * Реализация сервиса для работы с файлами мероприятий.
 */
@Service("meetingFileStoreService")
class MeetingFileStoreService @Autowired constructor(
    private val meetingService: MeetingService
) : BaseFileStoreService<Long>(
    pathOfStore = Path.of(MEETING_IMAGE_PATH),
    entityOfStorageName = "Meeting",
    rootImageUrl = "${SERVER_IMAGE_ROOT}/${MEETING_DIR_NAME}"
) {

    /**
     * Сохраняет переданный файл корневой директории с именем, переданного
     * идентификатора.
     * @param file сохраняемый файл.
     * @param id идентификатор мероприятия для которой сохраняется файл.
     * @return Возвращает url для получения файла.
     */
    override fun store(file: MultipartFile, id: Long): String =
        id.doIfExist(meetingService) { meeting ->
            val photoUrl = super.store(file, id)
            meeting.photoUrl = photoUrl
            meetingService.save(meeting)
            photoUrl
        }
}