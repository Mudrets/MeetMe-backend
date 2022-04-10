package com.meetme.file

import org.springframework.core.io.ByteArrayResource
import org.springframework.web.multipart.MultipartFile

interface FileStoreService<T> {

    fun isCorrectFile(file: MultipartFile): Boolean

    fun store(file: MultipartFile, id: T): String

    fun getImage(fileName: String): ByteArrayResource
}