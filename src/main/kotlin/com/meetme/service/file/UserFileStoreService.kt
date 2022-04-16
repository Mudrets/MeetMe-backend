package com.meetme.service.file

import com.meetme.util.doIfExist
import com.meetme.service.user.UserServiceImpl
import com.meetme.util.Constants.SERVER_IMAGE_ROOT
import com.meetme.util.Constants.USER_DIR_NAME
import com.meetme.util.Constants.USER_IMAGE_PATH
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Path

/**
 * Реализация сервиса для работы с файлами пользователя.
 */
@Service("userFileStoreService")
class UserFileStoreService @Autowired constructor(
    private val userService: UserServiceImpl,
) : BaseFileStoreService<Long>(
    pathOfStore = Path.of(USER_IMAGE_PATH),
    entityOfStorageName = "User",
    rootImageUrl = "${SERVER_IMAGE_ROOT}/${USER_DIR_NAME}"
) {
    /**
     * Сохраняет переданный файл корневой директории с именем, переданного
     * идентификатора.
     * @param file сохраняемый файл.
     * @param id идентификатор пользователя для которой сохраняется файл.
     * @return Возвращает url для получения файла.
     */
    override fun store(file: MultipartFile, id: Long): String =
        id.doIfExist(userService) { user ->
            val photoUrl = super.store(file, id)
            user.photoUrl = photoUrl
            userService.save(user)
            photoUrl
        }
}