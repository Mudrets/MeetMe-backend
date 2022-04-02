package com.meetme.chat.db

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany

@Entity(name = "chat")
class Chat(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "chat_id")
    val id: Long = 1,

    @OneToMany(targetEntity = Message::class, mappedBy = "chat")
    val messages: MutableList<Message> = mutableListOf()
)