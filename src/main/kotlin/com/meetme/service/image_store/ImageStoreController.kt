package com.meetme.service.image_store

import com.meetme.domain.dto.DataResponse
import com.meetme.service.file.FileStoreService
import com.meetme.util.tryExecute
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
class ImageStoreController {

    @Autowired
    private lateinit var imageStoreService: ImageStoreService

    private fun FileStoreService<*>.returnImage(fileName: String): ResponseEntity<Resource> {
        val inputStream = getImage(fileName)
        return ResponseEntity
            .status(HttpStatus.OK)
            .contentLength(inputStream.contentLength())
            .body(inputStream)
    }

    @PostMapping("/api/v1/imageStore/{meeting_id}")
    fun uploadImage(
        @PathVariable("meeting_id") meetingId: Long,
        @RequestParam("image") image: MultipartFile,
    ): DataResponse<List<String>> =
        tryExecute { imageStoreService.uploadImage(image, meetingId) }

    @GetMapping("/api/v1/imageStore/{meeting_id}")
    fun getImageStore(
        @PathVariable("meeting_id") meetingId: Long,
    ): DataResponse<List<String>> =
        tryExecute { imageStoreService.getImages(meetingId) }

    @GetMapping("/uploads/imageStore/{file_name}", produces = [MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE])
    fun getImageStoreImage(
        @PathVariable("file_name") fileName: String,
    ): ResponseEntity<Resource> = imageStoreService.returnImage(fileName)
}