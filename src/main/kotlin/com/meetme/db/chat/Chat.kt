package com.meetme.db.chat

import javax.persistence.*

@Entity(name = "chat")
class Chat(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "chat_id")
    val id: Long = 1,

    @OneToMany(targetEntity = Message::class, mappedBy = "chat", fetch = FetchType.EAGER)
    val messages: MutableList<Message> = mutableListOf()
)