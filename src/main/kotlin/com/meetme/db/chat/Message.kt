package com.meetme.db.chat

import com.meetme.db.user.User
import java.text.SimpleDateFormat
import java.util.*
import javax.persistence.*

@Entity(name = "messages")
class Message(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "message_id")
    val id: Long = 1,

    @Column(name = "content")
    var content: String = "",

    @Column(name = "timestamp")
    val timestamp: Long = 0,

    @ManyToOne(
        targetEntity = User::class,
        fetch = FetchType.EAGER,
        cascade = [CascadeType.ALL]
    )
    val sender: User = User(),

    @ManyToOne(
        targetEntity = Chat::class,
        fetch = FetchType.EAGER,
        cascade = [CascadeType.ALL]
    )
    val chat: Chat = Chat(),
) {
    val stringDate: String
        get() {
            val date = Date(timestamp)
            val format = SimpleDateFormat("MM-dd-yyyy HH:mm")
            return format.format(date)
        }
}