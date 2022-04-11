package com.meetme.service.file

import com.meetme.util.doIfExist
import com.meetme.service.user.UserServiceImpl
import com.meetme.util.Constants.SERVER_ROOT
import com.meetme.util.Constants.USER_DIR_NAME
import com.meetme.util.Constants.USER_IMAGE_PATH
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Path

@Service("userFileStoreService")
class UserFileStoreService : BaseFileStoreService<Long>(
    pathOfStore = Path.of(USER_IMAGE_PATH),
    entityOfStorageName = "User",
    rootImageUrl = "${SERVER_ROOT}/${USER_DIR_NAME}"
) {

    @Autowired
    private lateinit var userService: UserServiceImpl

    override fun store(file: MultipartFile, id: Long): String =
        id.doIfExist(userService) { user ->
            val photoUrl = super.store(file, id)
            user.photoUrl = photoUrl
            userService.save(user)
            photoUrl
        }
}