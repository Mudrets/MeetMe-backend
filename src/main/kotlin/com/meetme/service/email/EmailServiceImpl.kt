package com.meetme.service.email

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Component

@Component
class EmailServiceImpl @Autowired constructor(
    private val emailSender: JavaMailSender
) : EmailService {

    override fun sendMessage(to: String, subject: String, text: String) {
        val message = SimpleMailMessage()
        message.setFrom("noreply@baeldung.com")
        message.setTo(to)
        message.setSubject(subject)
        message.setText(text)
        emailSender.send(message)
    }
}