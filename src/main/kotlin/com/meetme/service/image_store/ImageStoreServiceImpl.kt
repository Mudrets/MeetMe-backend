package com.meetme.service.image_store

import com.meetme.util.doIfExist
import com.meetme.service.file.BaseFileStoreService
import com.meetme.db.image_store.Image
import com.meetme.db.image_store.ImageDao
import com.meetme.service.meeting.MeetingServiceImpl
import com.meetme.util.Constants
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Path

/**
 * Реализация сервиса для работы с хранилищем изображений.
 */
@Service
class ImageStoreServiceImpl(
    /**
     * Data access object для получения к данным в таблице изображений в базе данных.
     */
    private val imageDao: ImageDao,
    /**
     * Мервис для работы с мероприятиями.
     */
    private val meetingService: MeetingServiceImpl,
) : BaseFileStoreService<Long>(
    pathOfStore = Path.of(Constants.IMAGE_STORE_PATH),
    entityOfStorageName = "Image",
    rootImageUrl = "${Constants.SERVER_IMAGE_ROOT}/${Constants.IMAGE_STORE_DIR_NAME}"
), ImageStoreService {

    /**
     * Загружает изображение в хранилище.
     * @param image загружаемое изображение.
     * @param meetingId идентификатор мероприятия.
     * @return Возвращает список ссылок на загрузку изобрежений всего хранилища.
     */
    override fun uploadImage(image: MultipartFile, meetingId: Long): List<String> =
        meetingId.doIfExist(meetingService) { meeting ->
            val imageEntity = imageDao.save(Image(meeting = meeting))
            imageEntity.photoUrl = store(image, imageEntity.id)
            meeting.images.add(imageEntity)
            meetingService.save(meeting)
            meeting.images.map(Image::photoUrl)
        }

    /**
     * Получает изображение по названию из хранилища.
     * @param meetingId идентификатор мероприятия.
     * @return Возвращает список ссылок на загрузку изображений всего хранилища.
     */
    override fun getImages(meetingId: Long): List<String> =
        meetingId.doIfExist(meetingService) { meeting ->
            meeting.images.map(Image::photoUrl)
        }
}