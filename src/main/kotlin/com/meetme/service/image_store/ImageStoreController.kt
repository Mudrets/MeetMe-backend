package com.meetme.service.image_store

import com.meetme.domain.dto.DataResponse
import com.meetme.util.tryExecute
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/v1/imageStore")
class ImageStoreController {

    @Autowired
    private lateinit var imageStoreService: ImageStoreService

    @PostMapping("/{meeting_id}")
    fun uploadImage(
        @PathVariable("meeting_id") meetingId: Long,
        @RequestParam("image") image: MultipartFile,
    ): DataResponse<List<String>> =
        tryExecute { imageStoreService.uploadImage(image, meetingId) }

    @GetMapping("/{meeting_id}")
    fun getImageStore(
        @PathVariable("meeting_id") meetingId: Long,
    ): DataResponse<List<String>> =
        tryExecute { imageStoreService.getImages(meetingId) }
}