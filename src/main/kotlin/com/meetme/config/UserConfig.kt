package com.meetme.config

import com.meetme.auth.User
import com.meetme.auth.UserDao
import com.meetme.meeting.Meeting
import com.meetme.meeting.MeetingDao
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class UserConfig {

    @Bean
    fun userCommandLineRunner(userDao: UserDao, meetingDao: MeetingDao) = CommandLineRunner {
        val alex = User(email = "alex", password = "123456")
        val mariam = User(email = "mariam", password = "123456")

        val meeting1 = Meeting(
            name = "My birthday",
            description = "lol kek cheburek",
            endDate = "03-22-2022 23:59",
            startDate = "03-22-2022 00:00",
            admin = mariam,
        )
        mariam.meetings.add(meeting1)

        val meeting2 = Meeting(
            name = "Курсач",
            description = "очень очень сложна",
            endDate = "04-26-2022 23:59",
            startDate = "04-26-2022 00:00",
            admin = alex,
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