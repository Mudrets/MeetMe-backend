package com.meetme.image_store

import com.meetme.doIfExist
import com.meetme.file.FileStoreService
import com.meetme.image_store.db.Image
import com.meetme.image_store.db.ImageDao
import com.meetme.meeting.MeetingServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class ImageStoreServiceImpl : ImageStoreService {

    @Autowired
    private lateinit var imageDao: ImageDao

    @Autowired
    private lateinit var meetingService: MeetingServiceImpl

    @Autowired
    private lateinit var fileStoreService: FileStoreService

    override fun uploadImage(image: MultipartFile, meetingId: Long): List<String> =
        meetingId.doIfExist(meetingService) { meeting ->
            val imageEntity = saveImage(Image(meeting = meeting))
            imageEntity.photoUrl = fileStoreService.store(image, Image::class.java, imageEntity.id)
            meeting.images.add(imageEntity)
            meetingService.save(meeting)
            meeting.images.map(Image::photoUrl)
        }

    override fun getImages(meetingId: Long): List<String> =
        meetingId.doIfExist(meetingService) { meeting ->
            meeting.images.map(Image::photoUrl)
        }

    override fun saveImage(image: Image): Image = imageDao.save(image)


}