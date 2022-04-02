package com.meetme.image_store

import com.meetme.image_store.db.Image
import org.springframework.web.multipart.MultipartFile

interface ImageStoreService {

    fun uploadImage(image: MultipartFile, meetingId: Long): List<String>

    fun getImages(meetingId: Long): List<String>

    fun saveImage(image: Image): Image
}