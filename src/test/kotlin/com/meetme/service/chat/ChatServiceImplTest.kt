package com.meetme.service.chat

import com.meetme.db.chat.Chat
import com.meetme.db.chat.ChatDao
import com.meetme.db.chat.Message
import com.meetme.domain.dto.chat.GetMessagesRequestDto
import com.meetme.domain.dto.chat.SendMessageRequestDto
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.mock
import java.util.*
import kotlin.NoSuchElementException

@ExtendWith(MockitoExtension::class)
internal class ChatServiceImplTest {

    @Mock
    private lateinit var chatDao: ChatDao
    @Mock
    private lateinit var messageService: MessageService
    private lateinit var underTest: ChatServiceImpl

    companion object {

        private val testingChat = Chat(id = 1)
        private val testingMessage = Message(id = 1)
        private val testingChatWithMessages = Chat(id = 3)

        @JvmStatic
        @BeforeAll
        fun beforeAll() {
            testingChatWithMessages.messages.addAll(listOf(
                Message(5, "lol", 1, chat = testingChatWithMessages),
                Message(6, "kek", 2, chat = testingChatWithMessages),
                Message(7, "cheburek", 3, chat = testingChatWithMessages),
                Message(8, "", 4, chat = testingChatWithMessages),
                Message(9, "", 5, chat = testingChatWithMessages),
                Message(10, "", 6, chat = testingChatWithMessages),
            ))
        }
    }

    @BeforeEach
    fun setUp() {
        chatDao = mock(lenient = true) {
            on { save(any(Chat::class.java)) } doReturn testingChat
            on { findById(1) } doReturn Optional.of(testingChat)
            on { findById(2) } doReturn Optional.empty()
            on { findById(3) } doReturn Optional.of(testingChatWithMessages)
        }
        messageService = mock(lenient = true) {
            on { getMessage(1) } doReturn testingMessage
            on { getMessage(2) } doThrow NoSuchElementException()
            on { getMessage(7) } doReturn testingChatWithMessages.messages.find { it.id == 7L }!!
            on { sendMessage("Lol kek cheburek", 1, testingChat) } doReturn testingMessage
        }
        underTest = ChatServiceImpl(chatDao, messageService)
    }

    @Test
    fun `createChat should return new chat and save it in dao`() {
        underTest.createChat()
        verify(chatDao).save(any())
    }

    @Test
    fun `deleteChat should call delete from dao`() {
        val chat = underTest.createChat()
        underTest.deleteChat(chat)
        val chatArgumentCaptor = ArgumentCaptor.forClass(Chat::class.java)
        verify(chatDao).delete(chatArgumentCaptor.capture())
        assertEquals(chat, chatArgumentCaptor.value)
    }

    @Test
    fun `sendMessage to existing chat should create new message and save chat`() {
        val sendMessageRequestDto = SendMessageRequestDto(
            chatId = 1,
            content = "Lol kek cheburek",
            senderId = 1,
        )
        underTest.sendMessage(sendMessageRequestDto)
        val chatArgumentCaptor = ArgumentCaptor.forClass(Chat::class.java)
        verify(chatDao).findById(1)
        verify(chatDao).save(chatArgumentCaptor.capture())
        assertEquals(testingChat.id, chatArgumentCaptor.value.id)
        assertTrue(chatArgumentCaptor.value.messages.contains(testingMessage))
    }

    @Test
    fun `sendMessage to not existing chat should throw NoSuchElementException`() {
        val sendMessageRequestDto = SendMessageRequestDto(
            chatId = 2,
            content = "",
            senderId = 1,
        )
        assertThrowsExactly(NoSuchElementException::class.java) {
            underTest.sendMessage(sendMessageRequestDto)
        }
    }

    @Test
    fun `getMessages five with anchor 0 should return last five messages`() {
        val requestData = GetMessagesRequestDto(
            anchor = 0,
            messagesNumber = 5,
            chatId = 3
        )
        val messages = underTest.getMessages(requestData)
        verify(chatDao).findById(3)
        assertIterableEquals(messages, testingChatWithMessages.messages.sortedByDescending(Message::timestamp).subList(0, 5))
    }

    @Test
    fun `getMessages 100 with anchor 0 should return last all messages`() {
        val requestData = GetMessagesRequestDto(
            anchor = 0,
            messagesNumber = 100,
            chatId = 3
        )
        val messages = underTest.getMessages(requestData)
        verify(chatDao).findById(3)
        assertIterableEquals(messages, testingChatWithMessages.messages.sortedByDescending(Message::timestamp))
    }

    @Test
    fun `getMessages three with anchor 8 should return messages with ids 7, 6, 5`() {
        val requestData = GetMessagesRequestDto(
            anchor = 8,
            messagesNumber = 3,
            chatId = 3
        )
        val messages = underTest.getMessages(requestData)
        verify(chatDao).findById(3)
        assertIterableEquals(messages, testingChatWithMessages.messages.subList(0, 3).reversed())
    }

    @Test
    fun `getMessages three incorrect messagesNumber`() {
        val requestData = GetMessagesRequestDto(
            anchor = 8,
            messagesNumber = -1,
            chatId = 3
        )
        assertThrowsExactly(IllegalArgumentException::class.java) {
            underTest.getMessages(requestData)
        }
        verify(chatDao).findById(3)
    }

    @Test
    fun `getMessages for not existing chat`() {
        val requestData = GetMessagesRequestDto(
            anchor = 8,
            messagesNumber = 3,
            chatId = 5
        )
        assertThrowsExactly(NoSuchElementException::class.java) {
            underTest.getMessages(requestData)
        }
        verify(chatDao).findById(5)
    }

    @Test
    fun `deleteMessage with correct message id`() {
        val message = testingChatWithMessages.messages[2]
        underTest.deleteMessage(7)
        verify(messageService).getMessage(7)
        assertFalse(testingChatWithMessages.messages.contains(message))
        val chatArgumentCaptor = ArgumentCaptor.forClass(Chat::class.java)
        verify(chatDao).save(chatArgumentCaptor.capture())
        assertEquals(chatArgumentCaptor.value, testingChatWithMessages)
        testingChatWithMessages.messages.add(message)
    }

    @Test
    fun `deleteMessage with incorrect message id`() {
        assertThrowsExactly(NoSuchElementException::class.java) {
            underTest.deleteMessage(2)
        }
        verify(messageService).getMessage(2)
    }
}