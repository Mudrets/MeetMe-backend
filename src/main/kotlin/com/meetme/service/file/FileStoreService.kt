package com.meetme.service.file

import org.springframework.core.io.ByteArrayResource
import org.springframework.web.multipart.MultipartFile

/**
 * Сервис для работы с файлами.
 */
interface FileStoreService<T> {

    /**
     * Проверяет ялвяется ли файл корректным.
     * @param file сохраняемый файл.
     * @return Возвращает True если файл является корректным и False
     * в противном случае.
     */
    fun isCorrectFile(file: MultipartFile): Boolean

    /**
     * Сохраняет переданный файл корневой директории с именем, переданного
     * идентификатора.
     * @param file сохраняемый файл.
     * @param id идентификатор сущности для которой сохраняется файл.
     * @return Возвращает url для получения файла.
     */
    fun store(file: MultipartFile, id: T): String

    /**
     * Получает изображение по переданному названию.
     * @param fileName название файла.
     * @return Возвращет ByteArrayResource требуемого файла.
     */
    fun getImage(fileName: String): ByteArrayResource
}