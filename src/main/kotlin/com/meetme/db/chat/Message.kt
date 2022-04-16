package com.meetme.db.chat

import com.meetme.db.user.User
import java.text.SimpleDateFormat
import java.util.*
import javax.persistence.*

/**
 * Хранит ифнормацию о сообщении. Представляет из себя модель таблицы в базе данных.
 */
@Entity(name = "messages")
class Message(
    /**
     * Идентификатор сообщения.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "message_id")
    val id: Long = 1,

    /**
     * Контент сообщения.
     */
    @Column(name = "content")
    var content: String = "",

    /**
     * Время отправления сообщения, представленное в количество пройденных секунд
     * с 1 января 1970 года.
     */
    @Column(name = "timestamp")
    val timestamp: Long = 0,

    /**
     * Пользователь отправивший сообщение.
     */
    @ManyToOne(
        targetEntity = User::class,
        fetch = FetchType.EAGER,
        cascade = [CascadeType.ALL]
    )
    val sender: User = User(),

    /**
     * Чат в котором было отправленно сообщение.
     */
    @ManyToOne(
        targetEntity = Chat::class,
        fetch = FetchType.EAGER,
        cascade = [CascadeType.ALL]
    )
    val chat: Chat = Chat(),
) {
    /**
     * Предосталвяет дату отправления сообщения в формате "MM-dd-yyyy HH:MM"
     */
    val stringDate: String
        get() {
            val date = Date(timestamp)
            val format = SimpleDateFormat("MM-dd-yyyy HH:mm")
            return format.format(date)
        }
}