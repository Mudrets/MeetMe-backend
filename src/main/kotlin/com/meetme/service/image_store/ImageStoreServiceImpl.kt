package com.meetme.service.image_store

import com.meetme.util.doIfExist
import com.meetme.db.chat.file.BaseFileStoreService
import com.meetme.db.image_store.Image
import com.meetme.db.image_store.ImageDao
import com.meetme.service.meeting.MeetingServiceImpl
import com.meetme.util.Constants
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Path

@Service
class ImageStoreServiceImpl : BaseFileStoreService<Long>(
    pathOfStore = Path.of(Constants.IMAGE_STORE_PATH),
    entityOfStorageName = "Image",
    rootImageUrl = "${Constants.SERVER_ROOT}/${Constants.IMAGE_STORE_DIR_NAME}"
), ImageStoreService {

    @Autowired
    private lateinit var imageDao: ImageDao

    @Autowired
    private lateinit var meetingService: MeetingServiceImpl

    override fun uploadImage(image: MultipartFile, meetingId: Long): List<String> =
        meetingId.doIfExist(meetingService) { meeting ->
            val imageEntity = imageDao.save(Image(meeting = meeting))
            imageEntity.photoUrl = store(image, imageEntity.id)
            meeting.images.add(imageEntity)
            meetingService.save(meeting)
            meeting.images.map(Image::photoUrl)
        }

    override fun getImages(meetingId: Long): List<String> =
        meetingId.doIfExist(meetingService) { meeting ->
            meeting.images.map(Image::photoUrl)
        }
}