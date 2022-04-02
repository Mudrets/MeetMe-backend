package com.meetme.image_store.db

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository("imageRepository")
interface ImageDao : JpaRepository<Image, Long> {
}