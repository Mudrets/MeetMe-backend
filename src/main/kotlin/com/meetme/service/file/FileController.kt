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

/**
 * Контроллер, обрабатывающий запросы для работы с файлами.
 */
@RestController
@RequestMapping("/")
class FileController @Autowired constructor(
    /**
     * Сервис для работы с изображениями мероприятий.
     */
    @Qualifier("meetingFileStoreService")
    private val meetingFileStoreService: FileStoreService<Long>,
    /**
     * Сервис для работы с изображениями групп.
     */
    @Qualifier("groupFileStoreService")
    private val groupFileStoreService: FileStoreService<Long>,
    /**
     * Сервис для работы с изображениями пользователей.
     */
    @Qualifier("userFileStoreService")
    private val userFileStoreService: FileStoreService<Long>,
    /**
     * Сервис для работы с мероприятиями.
     */
    private val meetingService: MeetingService,
    /**
     * Сервис для работы с групп.
     */
    private val groupService: GroupService,
    /**
     * Сервис для работы с пользователями.
     */
    private val userService: UserService,
    /**
     * Маппер, преобразующий Meeting в MeetingDto.
     */
    private val meetingToMeetingDto: MeetingToMeetingDto,
    /**
     * Маппер, преобразующий User в UserDto.
     */
    private val userToUserDto: UserToUserDto,
    /**
     * Маппер, преобразующий Group в GroupDto.
     */
    private val groupToGroupDto: GroupToGroupDto,
) {

    /**
     * Возвращает файл в необходимом для клиентского приложения виде.
     * @param fileName название файла.
     */
    private fun FileStoreService<*>.returnImage(fileName: String): ResponseEntity<Resource> {
        val inputStream = getImage(fileName)
        return ResponseEntity
            .status(HttpStatus.OK)
            .contentLength(inputStream.contentLength())
            .body(inputStream)
    }

    /**
     * Обработчик HTTP GЕT запроса по url /uploads/meetings/{file_name} для получения изображения с сервера.
     * @param fileName название файла.
     */
    @GetMapping("/uploads/meetings/{file_name}", produces = [MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE])
    fun getMeetingImage(
        @PathVariable("file_name") fileName: String,
    ): ResponseEntity<Resource> = meetingFileStoreService.returnImage(fileName)

    /**
     * Обработчик HTTP GЕT запроса по url /uploads/groups/{file_name} для получения изображения с сервера.
     * @param fileName название файла.
     */
    @GetMapping("/uploads/groups/{file_name}", produces = [MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE])
    fun getGroupImage(
        @PathVariable("file_name") fileName: String,
    ): ResponseEntity<Resource> = groupFileStoreService.returnImage(fileName)

    /**
     * Обработчик HTTP GЕT запроса по url /uploads/users/{file_name} для получения изображения с сервера.
     * @param fileName название файла.
     */
    @GetMapping("/uploads/users/{file_name}", produces = [MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE])
    fun getUsersImage(
        @PathVariable("file_name") fileName: String,
    ): ResponseEntity<Resource> = userFileStoreService.returnImage(fileName)

    /**
     * Обработчик HTTP POST запроса по url /api/v1/meetings/{meeting_id}/image для загрузки изображения на сервер.
     * @param image загружаемое изображение.
     * @param meetingId идентификатор мероприятия для которого загружается изображение.
     */
    @PostMapping("/api/v1/meetings/{meeting_id}/image")
    fun uploadMeetingImage(
        @RequestParam("image") image: MultipartFile,
        @PathVariable("meeting_id") meetingId: Long,
    ): DataResponse<MeetingDto> =
        tryExecute {
            meetingFileStoreService.store(image, meetingId)
            meetingToMeetingDto(meetingService.get(meetingId), Constants.NON_EXISTENT_USER_ID)
        }

    /**
     * Обработчик HTTP POST запроса по url /api/v1/user/{user_id}/image для загрузки изображения на сервер.
     * @param image загружаемое изображение.
     * @param userId идентификатор пользователя для которого загружается изображение.
     */
    @PostMapping("/api/v1/user/{user_id}/image")
    fun uploadUserImage(
        @RequestParam("image") image: MultipartFile,
        @PathVariable("user_id") userId: Long,
    ): DataResponse<UserDto> =
        tryExecute {
            userFileStoreService.store(image, userId)
            userToUserDto(userService.get(userId))
        }

    /**
     * Обработчик HTTP POST запроса по url /api/v1/groups/{group_id}/image для загрузки изображения на сервер.
     * @param image загружаемое изображение.
     * @param groupId идентификатор группы для которой загружается изображение.
     */
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