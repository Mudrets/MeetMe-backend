package com.meetme.config

import com.meetme.services.auth.User
import com.meetme.services.auth.UserDao
import com.meetme.services.chat.Chat
import com.meetme.services.chat.ChatDao
import com.meetme.services.meeting.Meeting
import com.meetme.services.meeting.MeetingDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
class UserConfig {

    @Autowired
    private lateinit var passwordEncoder: BCryptPasswordEncoder

    @Bean
    fun userCommandLineRunner(userDao: UserDao, meetingDao: MeetingDao, chatDao: ChatDao) = CommandLineRunner {
        val alex = User(email = "lol@kek.com", password = passwordEncoder.encode("123456"))
        val mariam = User(email = "mariam", password = passwordEncoder.encode("123456"))

        val chat1 = Chat()
        val chat2 = Chat()
        chatDao.saveAll(
            listOf(chat1, chat2)
        )


        val meeting1 = Meeting(
            name = "My birthday",
            description = "lol kek cheburek",
            endDate = "03-22-2022 23:59",
            startDate = "03-22-2022 00:00",
            admin = mariam,
            chat = chat1
        )
        mariam.meetings.add(meeting1)

        val meeting2 = Meeting(
            name = "Курсач",
            description = "очень очень сложна",
            endDate = "04-26-2022 23:59",
            startDate = "04-26-2022 00:00",
            admin = alex,
            chat = chat2
        )
        alex.meetings.add(meeting2)

        userDao.saveAll(
            listOf(alex, mariam)
        )

        meetingDao.saveAll(
            listOf(meeting1, meeting2)
        )
    }
}