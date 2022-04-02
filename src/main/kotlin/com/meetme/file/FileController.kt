package com.meetme.file

import com.meetme.file.FileStoreService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping


@Controller
@RequestMapping("/uploads")
class FileController {

    @Autowired
    private lateinit var fileStoreService: FileStoreService

    @GetMapping("/{dir_name}/{file_name}", produces = [MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE])
    fun getImage(
        @PathVariable("dir_name") dirName: String,
        @PathVariable("file_name") fileName: String,
    ): ResponseEntity<Resource> {
        val inputStream = fileStoreService.getImage(dirName, fileName)
        return ResponseEntity
            .status(HttpStatus.OK)
            .contentLength(inputStream.contentLength())
            .body(inputStream)
    }
}