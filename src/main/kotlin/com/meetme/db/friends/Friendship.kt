package com.meetme.db.friends

import com.meetme.db.user.User
import javax.persistence.*

/**
 * Дружеские отношения между двумя пользователями.  Представляет из себя
 * модель таблицы в базе данных.
 */
@Entity
class Friendship(
    /**
     * Идентификатор отношений.
     */
    @Id
    @SequenceGenerator(
        name = "friendship_sequence",
        sequenceName = "friendship_sequence",
        allocationSize = 1,
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "friendship_sequence"
    )
    @Column(name = "friendship_id")
    val id: Long = 0,

    /**
     * Первый пользователь отношений.
     */
    @ManyToOne(targetEntity = User::class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user1_id")
    val user1: User = User(),

    /**
     * Второй пользователь отношений.
     */
    @ManyToOne(targetEntity = User::class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user2_id")
    val user2: User = User(),

    /**
     * Флаг подтверждения дружбы от первого пользователя.
     */
    @Column(name = "accept_from_user1")
    var acceptFromUser1: Boolean = false,

    /**
     * Флаг подтверждения дружбы от второго пользователя.
     */
    @Column(name = "accept_from_user2")
    var acceptFromUser2: Boolean = false,
) {
    /**
     * Проверяет, что отношение подтвержденно двумя пользозвателями,
     * то есть между ними есть дружеские отношения.
     * @return Возвращает True если пользователи являются друзьями и False в противном случае.
     */
    val isFriendship: Boolean
        get() = acceptFromUser1 && acceptFromUser2

    /**
     * Проверяет, что заявка в друзья была отменена двумя пользователями.
     * @return Возвращает True если оба пользователя отклонили или отозвали заявку
     * в друзья и False в противном случае.
     */
    val canceledFriendship: Boolean
        get() = !acceptFromUser1 && !acceptFromUser2

    /**
     * Проверяет, что запрос на дружбу отправлен пользователю.
     * @param user проверяемый пользователь.
     * @return Возвращает True если пользователю отправлен запрос в друзья,
     * но он его еще не принял, и False в противном случае.
     */
    fun isFriendRequestToUser(user: User): Boolean =
        user1 == user && acceptFromUser2 && !acceptFromUser1 ||
            user2 == user && acceptFromUser1 && !acceptFromUser2

    /**
     * Проверяет, что запрос на дружбу отправлен от пользователя.
     * @param user проверяемый пользователь.
     * @return Возвращает True если пользователь отправил запрос в друзья,
     * но он еще не принят, и False в противном случае.
     */
    fun isFriendRequestFromUser(user: User): Boolean =
        user1 == user && acceptFromUser1 && !acceptFromUser2 ||
            user2 == user && acceptFromUser2 && !acceptFromUser1

    /**
     * Принимает или отправляет запрос в друзья от лица пользователя.
     * @param user пользователь, от лица которого отправлен или принят запрос.
     */
    fun setAcceptFrom(user: User) {
        if (user1 == user)
            acceptFromUser1 = true
        else if (user2 == user)
            acceptFromUser2 = true
    }

    /**
     * Отклоняет или отзывает запрос в друзья от лица пользователя.
     * @param user пользователь, от лица которого отклонен или отозван запрос.
     */
    fun cancelAcceptFor(user: User) {
        if (user1 == user)
            acceptFromUser1 = false
        else if (user2 == user)
            acceptFromUser2 = false
    }

    /**
     * Возвращает пользователя для с которым есть некоторые дружеские отношения.
     */
    fun getFriend(user: User) =
        if (user1 == user) user2 else user1
}