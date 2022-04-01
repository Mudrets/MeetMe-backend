package com.meetme.services.chat.message

import com.meetme.services.auth.User
import com.meetme.services.chat.Chat
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
)