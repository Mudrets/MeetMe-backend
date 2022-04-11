package com.meetme.service.file

import com.meetme.service.user.UserServiceImpl
import com.meetme.util.Constants
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.io.ByteArrayResource
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path

open class BaseFileStoreService<T>(
    private val pathOfStore: Path,
    private val entityOfStorageName: String,
    private val rootImageUrl: String,
) : FileStoreService<T> {

    private val logger: Logger = LoggerFactory.getLogger(UserServiceImpl::class.java)

    init {
        if (!Files.exists(rootLocation))
            Files.createDirectory(rootLocation)

        if (!Files.exists(pathOfStore))
            Files.createDirectory(pathOfStore)
    }

    private fun isSupportedContentType(contentType: String): Boolean {
        return contentType.endsWith(".png") ||
            contentType.endsWith(".jpg") ||
            contentType.endsWith(".jpeg")
    }

    private fun getImageUrl(id: T) = "$rootImageUrl/$id"

    override fun isCorrectFile(file: MultipartFile): Boolean {
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

    override fun store(file: MultipartFile, id: T): String {
        if (!isCorrectFile(file))
            throw IllegalArgumentException("Incorrect file")

        try {
            val path = pathOfStore.resolve(id.toString())
            Files.deleteIfExists(path)
            Files.copy(file.inputStream, path)
            logger.debug("$entityOfStorageName's image with id = $id uploaded in the storage by path ")
            return getImageUrl(id)
        } catch (io: IOException) {
            logger.error("failed to save image: $io")
            throw io
        }
    }

    override fun getImage(fileName: String): ByteArrayResource {
        val path = pathOfStore.resolve(fileName)
        if (!Files.exists(path))
            throw IllegalArgumentException("File by path $path does not exist")
        return ByteArrayResource(Files.readAllBytes(path))
    }

    companion object {
        private val rootLocation = Path.of(Constants.ROOT_IMAGE_DIR)
    }
}