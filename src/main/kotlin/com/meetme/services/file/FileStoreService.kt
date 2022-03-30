package com.meetme.services.file

import com.meetme.contorller.FileController
import com.meetme.services.auth.User
import com.meetme.services.auth.UserService
import com.meetme.services.group.Group
import com.meetme.services.meeting.Meeting
import com.meetme.util.Constants.GROUP_DIR_NAME
import com.meetme.util.Constants.GROUP_IMAGE_PATH
import com.meetme.util.Constants.MEETING_DIR_NAME
import com.meetme.util.Constants.MEETING_IMAGE_PATH
import com.meetme.util.Constants.ROOT_IMAGE_DIR
import com.meetme.util.Constants.SERVER_ROOT
import com.meetme.util.Constants.USER_DIR_NAME
import com.meetme.util.Constants.USER_IMAGE_PATH
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.io.ByteArrayResource
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path

@Service
class FileStoreService {

    private val logger: Logger = LoggerFactory.getLogger(UserService::class.java)

    private fun getPath(entityClass: Class<*>, id: Long): Path =
        when (entityClass) {
            User::class.java -> userRootLocation.resolve("$id.png")
            Meeting::class.java -> meetingRootLocation.resolve("$id.png")
            Group::class.java -> groupRootLocation.resolve("$id.png")
            else -> rootLocation.resolve("$id.png")
        }

    private fun getImageUrl(entityClass: Class<*>, id: Long): String =
        when (entityClass) {
            User::class.java -> "$SERVER_ROOT/$USER_IMAGE_PATH/$id.png"
            Meeting::class.java -> "$SERVER_ROOT/$MEETING_IMAGE_PATH/$id.png"
            Group::class.java -> "$SERVER_ROOT/$GROUP_IMAGE_PATH/$id.png"
            else -> "$SERVER_ROOT/$ROOT_IMAGE_DIR/$id.png"
        }

    fun correctFile(file: MultipartFile): Boolean {
        if (file.isEmpty || file.originalFilename.isNullOrEmpty()) {
            return false
        }

        if (!isSupportedContentType(file.originalFilename!!)) {
            logger.debug("File is not an image.")
            return false
        }

        logger.debug("File is correct")
        return true
    }

    private fun isSupportedContentType(contentType: String): Boolean {
        return contentType.endsWith(".png") ||
            contentType.endsWith(".jpg") ||
            contentType.endsWith(".jpeg")
    }

    fun store(file: MultipartFile, entityClass: Class<*>, id: Long): String {
        if (!correctFile(file))
            throw IllegalArgumentException("Incorrect file")

        try {
            val path = getPath(entityClass, id)
            Files.deleteIfExists(path)
            Files.copy(file.inputStream, path)
            logger.debug("${entityClass.simpleName}'s image with id = $id uploaded in the storage by path ")
            return getImageUrl(entityClass, id)
        } catch (io: IOException) {
            logger.error("failed to save image: $io")
            throw io
        }
    }

    fun getImage(dirName: String, fileName: String): ByteArrayResource {
        val path = when (dirName) {
            "meetings" -> meetingRootLocation.resolve(fileName)
            "users" -> userRootLocation.resolve(fileName)
            "groups" -> groupRootLocation.resolve(fileName)
            else -> rootLocation.resolve(fileName)
        }
        if (!Files.exists(path))
            throw IllegalArgumentException("File by path $path does not exist")
        return ByteArrayResource(Files.readAllBytes(path))
    }

    companion object {
        private val rootLocation = Path.of(ROOT_IMAGE_DIR)
        private val userRootLocation = Path.of(USER_IMAGE_PATH)
        private val meetingRootLocation = Path.of(MEETING_IMAGE_PATH)
        private val groupRootLocation = Path.of(GROUP_IMAGE_PATH)

        fun init() {
            if (!Files.exists(rootLocation)) {
                Files.createDirectory(rootLocation)
            }
            if (!Files.exists(userRootLocation)) {
                Files.createDirectory(userRootLocation)
            }
            if (!Files.exists(meetingRootLocation)) {
                Files.createDirectory(meetingRootLocation)
            }
            if (!Files.exists(groupRootLocation)) {
                Files.createDirectory(groupRootLocation)
            }
        }
    }
}