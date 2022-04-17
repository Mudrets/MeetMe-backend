package com.meetme.config

import com.meetme.util.Constants.EMAIL_FOR_SENDING
import com.meetme.util.Constants.EMAIL_PASSWORD
import com.meetme.util.Constants.EMAIL_PORT
import com.meetme.util.Constants.HOST
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSenderImpl
import java.util.*

/**
 * Класс предоставляющий зависимости для отправки сообщений
 * на электронную почту.
 */
@Configuration
class EmailConfig {
    @Bean
    fun getJavaMailSender(): JavaMailSenderImpl {
        val mailSender = JavaMailSenderImpl()
        mailSender.host = HOST
        mailSender.port = EMAIL_PORT

        mailSender.username = EMAIL_FOR_SENDING
        mailSender.password = EMAIL_PASSWORD

        val props: Properties = mailSender.javaMailProperties
        props["mail.transport.protocol"] = "smtp"
        props["mail.smtp.auth"] = "true"
        props["mail.smtp.starttls.enable"] = "true"
        props["mail.debug"] = "true"
        props["X-Priority"] = "1"

        return mailSender
    }
}