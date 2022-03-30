package com.meetme.contorller

import org.apache.tomcat.util.http.fileupload.FileUtils
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths


@Controller
@RequestMapping("/uploads")
class FileController {

    @GetMapping("/{filename}", produces = [MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE])
    fun getImage(
        @PathVariable("filename") fileName: String
    ): ResponseEntity<Resource> {
        val inputStream = ByteArrayResource(Files.readAllBytes(rootLocation.resolve(fileName)))
        return ResponseEntity
            .status(HttpStatus.OK)
            .contentLength(inputStream.contentLength())
            .body(inputStream)
    }

    companion object {
        private val rootLocation: Path = Paths.get("uploads")
    }
}