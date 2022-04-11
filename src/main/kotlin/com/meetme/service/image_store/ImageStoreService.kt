package com.meetme.service.image_store

import org.springframework.web.multipart.MultipartFile

interface ImageStoreService {

    fun uploadImage(image: MultipartFile, meetingId: Long): List<String>

    fun getImages(meetingId: Long): List<String>
}