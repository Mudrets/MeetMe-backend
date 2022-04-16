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

/**
 * Базовая реализация сервиса для работы с файлами.
 */
open class BaseFileStoreService<T>(
    /**
     * Путь до места хранения файлов.
     */
    private val pathOfStore: Path,
    /**
     * Название сущности, которая использует файл.
     */
    private val entityOfStorageName: String,
    /**
     * Корневой url для получения файлов указанной сущности.
     */
    private val rootImageUrl: String,
) : FileStoreService<T> {

    /**
     * Логгер для логгирования.
     */
    private val logger: Logger = LoggerFactory.getLogger(UserServiceImpl::class.java)

    init {
        if (!Files.exists(rootLocation))
            Files.createDirectory(rootLocation)

        if (!Files.exists(pathOfStore))
            Files.createDirectory(pathOfStore)
    }

    /**
     * Проверяет является ли тип файла корректным для хранилища.
     * @param contentType тип файла.
     * @return Возвращает True если тип файла корректный и False в противном случае.
     */
    private fun isSupportedContentType(contentType: String): Boolean {
        return contentType.endsWith(".png") ||
            contentType.endsWith(".jpg") ||
            contentType.endsWith(".jpeg")
    }

    /**
     * Получает url к файлу по его идентификатору.
     * @param id идентификатор.
     */
    private fun getImageUrl(id: T) = "$rootImageUrl/$id.png"

    /**
     * Проверяет ялвяется ли файл корректным.
     * @param file сохраняемый файл.
     * @return Возвращает True если файл является корректным и False
     * в противном случае.
     */
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

    /**
     * Сохраняет переданный файл корневой директории с именем, переданного
     * идентификатора.
     * @param file сохраняемый файл.
     * @param id идентификатор сущности для которой сохраняется файл.
     * @return Возвращает url для получения файла.
     */
    override fun store(file: MultipartFile, id: T): String {
        if (!isCorrectFile(file))
            throw IllegalArgumentException("Incorrect file")

        try {
            val path = pathOfStore.resolve("$id.png")
            Files.deleteIfExists(path)
            Files.copy(file.inputStream, path)
            logger.debug("$entityOfStorageName's image with id = $id uploaded in the storage by path ")
            return getImageUrl(id)
        } catch (io: IOException) {
            logger.error("failed to save image: $io")
            throw io
        }
    }

    /**
     * Получает изображение по переданному названию.
     * @param fileName название файла.
     * @return Возвращет ByteArrayResource требуемого файла.
     */
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