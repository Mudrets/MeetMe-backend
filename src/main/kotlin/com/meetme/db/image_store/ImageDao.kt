package com.meetme.db.image_store

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Интерфейс предоставляющий абстракцию для работы с таблицей картинок
 * из хранилища фотографий в базе данных.
 */
@Repository("imageRepository")
interface ImageDao : JpaRepository<Image, Long>