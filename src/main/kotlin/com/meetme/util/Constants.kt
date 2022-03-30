package com.meetme.util

import io.jsonwebtoken.security.Keys
import javax.crypto.SecretKey

object Constants {
    // STORAGE
    const val ROOT_IMAGE_DIR = "uploads"
    const val USER_DIR_NAME = "users"
    const val MEETING_DIR_NAME = "meetings"
    const val GROUP_DIR_NAME = "groups"
    const val USER_IMAGE_PATH = "$ROOT_IMAGE_DIR/$USER_DIR_NAME"
    const val MEETING_IMAGE_PATH = "$ROOT_IMAGE_DIR/$MEETING_DIR_NAME"
    const val GROUP_IMAGE_PATH = "$ROOT_IMAGE_DIR/$GROUP_DIR_NAME"

    // SERVER
    const val SERVER_ROOT = "http://localhost:8080"

    //JWT
    private const val KEY = "securesecuresecuresecuresecuresecuresecuresecuresecuresecuresecuresecuresecuresecure"
    const val TOKEN_PREFIX = "Bearer "
    const val TOKEN_EXPIRATION_AFTER_DAYS = 14L
    val secretKey: SecretKey = Keys.hmacShaKeyFor(KEY.toByteArray())!!
}