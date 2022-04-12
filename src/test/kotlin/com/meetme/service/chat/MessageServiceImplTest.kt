package com.meetme.service.chat

import com.meetme.db.chat.Chat
import com.meetme.db.chat.Message
import com.meetme.db.chat.MessageDao
import com.meetme.db.user.User
import com.meetme.domain.dto.chat.SendMessageRequestDto
import com.meetme.service.user.UserService
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrowsExactly
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import java.util.*
import kotlin.NoSuchElementException

@ExtendWith(MockitoExtension::class)
internal class MessageServiceImplTest {

    @Mock private lateinit var messageDao: MessageDao
    @Mock private lateinit var userService: UserService
    private lateinit var underTest: MessageServiceImpl

    private val testMessage1 = Message(id = 1)
    private val testUser1 = User(id = 1)

    @BeforeEach
    fun setUp() {
        messageDao = mock(lenient = true) {
            on { save(any(Message::class.java)) } doReturn testMessage1
            on { findById(1) } doReturn Optional.of(testMessage1)
            on { findById(2) } doReturn Optional.empty()
        }
        userService = mock(lenient = true) {
            on { get(1) } doReturn testUser1
        }
        underTest = MessageServiceImpl(messageDao, userService)
    }

    @Test
    fun `send message should save message with passed vales`() {
        val chat = Chat(1)
        val content = "Lol"
        val userId = 1L
        underTest.sendMessage(chat = chat, content = content, userId = userId)
        val messageArgumentCaptor = ArgumentCaptor.forClass(Message::class.java)
        verify(messageDao).save(messageArgumentCaptor.capture())
        assertEquals(chat, messageArgumentCaptor.value.chat)
        assertEquals(content, messageArgumentCaptor.value.content)
        assertEquals(userId, messageArgumentCaptor.value.sender.id)
    }

    @Test
    fun `getMessage by existing id should get message from messageDao`() {
        val message = underTest.getMessage(1)
        verify(messageDao).findById(1)
        assertEquals(message, testMessage1)
    }

    @Test
    fun `getMessage by not existing id should throw NoSuchElementException`() {
        assertThrowsExactly(NoSuchElementException::class.java) {
            underTest.getMessage(2)
        }
        verify(messageDao).findById(2)
    }

    @Test
    fun `editMessage with existing id should change message's content and save message`() {
        val newContent = "lol kek cheburek"
        val id = 1L
        underTest.editMessage(messageId = id, newContent = newContent)
        verify(messageDao).findById(id)
        val messageArgumentCaptor = ArgumentCaptor.forClass(Message::class.java)
        verify(messageDao).save(messageArgumentCaptor.capture())
        assertEquals(newContent, messageArgumentCaptor.value.content)
    }

    @Test
    fun `editMessage with not existing id should throw NoSuchElementException`() {
        val newContent = "lol kek cheburek"
        val id = 2L
        assertThrowsExactly(NoSuchElementException::class.java) {
            underTest.editMessage(messageId = id, newContent = newContent)
        }
        verify(messageDao).findById(id)
    }

    @Test
    fun `deleteMessage should call delete by id for messageDao`() {
        underTest.deleteMessage(1)
        verify(messageDao).deleteById(1)
    }
}