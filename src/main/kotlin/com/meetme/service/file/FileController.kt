package com.meetme.service.file

import com.meetme.domain.dto.DataResponse
import com.meetme.domain.dto.auth.UserDto
import com.meetme.domain.dto.goup.GroupDto
import com.meetme.domain.dto.meeting.MeetingDto
import com.meetme.service.group.GroupService
import com.meetme.service.group.mapper.GroupToGroupDto
import com.meetme.service.meeting.MeetingService
import com.meetme.service.meeting.mapper.MeetingToMeetingDto
import com.meetme.service.user.UserService
import com.meetme.service.user.mapper.UserToUserDto
import com.meetme.util.Constants
import com.meetme.util.tryExecute
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.core.io.Resource
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/")
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

    @Autowired
    private lateinit var meetingService: MeetingService

    @Autowired
    private lateinit var groupService: GroupService

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var meetingToMeetingDto: MeetingToMeetingDto

    @Autowired
    private lateinit var userToUserDto: UserToUserDto

    @Autowired
    private lateinit var groupToGroupDto: GroupToGroupDto

    private fun FileStoreService<*>.returnImage(fileName: String): ResponseEntity<Resource> {
        val inputStream = getImage(fileName)
        return ResponseEntity
            .status(HttpStatus.OK)
            .contentLength(inputStream.contentLength())
            .body(inputStream)
    }

    @GetMapping("/uploads/meetings/{file_name}", produces = [MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE])
    fun getMeetingImage(
        @PathVariable("file_name") fileName: String,
    ): ResponseEntity<Resource> = meetingFileStoreService.returnImage(fileName)

    @GetMapping("/uploads/groups/{file_name}", produces = [MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE])
    fun getGroupImage(
        @PathVariable("file_name") fileName: String,
    ): ResponseEntity<Resource> = groupFileStoreService.returnImage(fileName)

    @GetMapping("/uploads/users/{file_name}", produces = [MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE])
    fun getUsersImage(
        @PathVariable("file_name") fileName: String,
    ): ResponseEntity<Resource> = userFileStoreService.returnImage(fileName)

    @PostMapping("/api/v1/meetings/{meeting_id}/image")
    fun uploadMeetingImage(
        @RequestParam("image") image: MultipartFile,
        @PathVariable("meeting_id") meetingId: Long,
    ): DataResponse<MeetingDto> =
        tryExecute {
            meetingFileStoreService.store(image, meetingId)
            meetingToMeetingDto(meetingService.get(meetingId), Constants.NON_EXISTENT_USER_ID)
        }

    @PostMapping("/api/v1/user/{user_id}/image")
    fun uploadUserImage(
        @RequestParam("image") image: MultipartFile,
        @PathVariable("user_id") userId: Long,
    ): DataResponse<UserDto> =
        tryExecute {
            userFileStoreService.store(image, userId)
            userToUserDto(userService.get(userId))
        }

    @PostMapping("/api/v1/groups/{group_id}/image")
    fun uploadGroupImage(
        @RequestParam("image") image: MultipartFile,
        @PathVariable("group_id") groupId: Long,
    ): DataResponse<GroupDto> =
        tryExecute {
            groupFileStoreService.store(image, groupId)
            groupToGroupDto(groupService.get(groupId))
        }
}