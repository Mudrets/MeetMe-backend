package com.meetme.db.chat

import javax.persistence.*

/**
 * Хранит информацию о чате. Представляет из себя модель таблицы в базе данных.
 * @param id id чата;
 * @param messages список сообщений чата.
 */
@Entity(name = "chat")
class Chat(
    /**
     * Идентификатор чата.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "chat_id")
    val id: Long = 1,

    /**
     * Список сообщений чата.
     */
    @OneToMany(targetEntity = Message::class, mappedBy = "chat", fetch = FetchType.EAGER)
    val messages: MutableList<Message> = mutableListOf()
)