package com.meetme.file

import com.meetme.domain.dto.DataResponse
import com.meetme.domain.dto.meeting.MeetingDto
import com.meetme.tryExecute
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile


@Controller
@RequestMapping("/uploads")
class FileController {

    @Qualifier("meetingFileStoreService")
    @Autowired
    private lateinit var meetingFileStoreService: FileStoreService<Long>

    @Qualifier("groupFileStoreService")
    @Autowired
    private lateinit var groupFileStoreService: FileStoreService<Long>

    @Qualifier("userFileStoreService")
    @Autowired
    private lateinit var userFileStoreService: FileStoreService<Long>

    private fun FileStoreService<*>.returnImage(fileName: String): ResponseEntity<Resource> {
        val inputStream = getImage(fileName)
        return ResponseEntity
            .status(HttpStatus.OK)
            .contentLength(inputStream.contentLength())
            .body(inputStream)
    }

    @GetMapping("/meetings/{file_name}", produces = [MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE])
    fun getMeetingImage(
        @PathVariable("file_name") fileName: String,
    ): ResponseEntity<Resource> = meetingFileStoreService.returnImage(fileName)

    @GetMapping("/groups/{file_name}", produces = [MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE])
    fun getGroupImage(
        @PathVariable("file_name") fileName: String,
    ): ResponseEntity<Resource> = groupFileStoreService.returnImage(fileName)

    @GetMapping("/users/{file_name}", produces = [MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE])
    fun getUsersImage(
        @PathVariable("file_name") fileName: String,
    ): ResponseEntity<Resource> = userFileStoreService.returnImage(fileName)

    @PostMapping("/meetings/{meeting_id}")
    fun uploadMeetingImage(
        @RequestParam("image") image: MultipartFile,
        @PathVariable("meeting_id") meetingId: Long,
    ): DataResponse<String> =
        tryExecute {
            meetingFileStoreService.store(image, meetingId)
        }

    @PostMapping("/users/{user_id}")
    fun uploadUserImage(
        @RequestParam("image") image: MultipartFile,
        @PathVariable("user_id") userId: Long,
    ): DataResponse<String> =
        tryExecute {
            userFileStoreService.store(image, userId)
        }

    @PostMapping("/groups/{group_id}")
    fun uploadGroupImage(
        @RequestParam("image") image: MultipartFile,
        @PathVariable("group_id") groupId: Long,
    ): DataResponse<String> =
        tryExecute {
            groupFileStoreService.store(image, groupId)
        }
}