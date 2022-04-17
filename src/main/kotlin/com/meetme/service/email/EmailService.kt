package com.meetme.service.email

interface EmailService {
    fun sendMessage(to: String, subject: String, text: String)
}