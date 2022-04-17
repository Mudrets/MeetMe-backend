package com.meetme.util

import io.jsonwebtoken.security.Keys
import javax.crypto.SecretKey

object Constants {
    // STORAGE
    const val ROOT_IMAGE_DIR = "uploads"
    const val USER_DIR_NAME = "users"
    const val MEETING_DIR_NAME = "meetings"
    const val GROUP_DIR_NAME = "groups"
    const val IMAGE_STORE_DIR_NAME = "imageStore"
    const val USER_IMAGE_PATH = "$ROOT_IMAGE_DIR/$USER_DIR_NAME"
    const val MEETING_IMAGE_PATH = "$ROOT_IMAGE_DIR/$MEETING_DIR_NAME"
    const val GROUP_IMAGE_PATH = "$ROOT_IMAGE_DIR/$GROUP_DIR_NAME"
    const val IMAGE_STORE_PATH = "$ROOT_IMAGE_DIR/$IMAGE_STORE_DIR_NAME"

    // SERVER
    const val SERVER_ROOT = "http://localhost:8080"
    const val SERVER_IMAGE_ROOT = "$SERVER_ROOT/uploads"

    //JWT
    private const val KEY = "securesecuresecuresecuresecuresecuresecuresecuresecuresecuresecuresecuresecuresecure"
    const val TOKEN_PREFIX = "Bearer "
    const val TOKEN_EXPIRATION_AFTER_DAYS = 21L
    val secretKey: SecretKey = Keys.hmacShaKeyFor(KEY.toByteArray())!!

    //EMAIL
    const val HOST = "smtp.gmail.com"
    const val EMAIL_PORT = 587
    const val EMAIL_FOR_SENDING = "meetme.info.noreply@gmail.com"
    const val EMAIL_PASSWORD = "meetme2604"

    //USER
    const val NON_EXISTENT_USER_ID = -1L
}