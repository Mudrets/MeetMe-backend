package com.meetme.service.image_store

import com.meetme.service.file.FileStoreService
import org.springframework.web.multipart.MultipartFile

/**
 * Сервис для работы с хранилищем изображений.
 */
interface ImageStoreService : FileStoreService<Long> {

    /**
     * Загружает изображение в хранилище.
     * @param image загружаемое изображение.
     * @param meetingId идентификатор мероприятия.
     * @return Возвращает список ссылок на загрузку изобрежений всего хранилища.
     */
    fun uploadImage(image: MultipartFile, meetingId: Long): List<String>

    /**
     * Получает изображение по названию из хранилища.
     * @param meetingId идентификатор мероприятия.
     * @return Возвращает список ссылок на загрузку изображений всего хранилища.
     */
    fun getImages(meetingId: Long): List<String>
}