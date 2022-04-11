package com.meetme.service.image_store

import com.meetme.service.file.FileStoreService
import org.springframework.web.multipart.MultipartFile

interface ImageStoreService : FileStoreService<Long> {

    fun uploadImage(image: MultipartFile, meetingId: Long): List<String>

    fun getImages(meetingId: Long): List<String>
}