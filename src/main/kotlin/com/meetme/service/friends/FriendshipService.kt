package com.meetme.service.friends

import com.meetme.service.friends.enums.FriendshipStatus
import com.meetme.db.user.User

/**
 * Сервис для работы с заявками в друзья.
 */
interface FriendshipService {

    /**
     * Получает список заявок в друзья пользователю с переданным идентификатором.
     * @param userId идентификатор пользователя.
     * @return Возвращает список пользователей, от которых была отпралвена заявка в
     * друзья пользователю с переданным идентификатором.
     */
    fun getFriendRequestToUser(userId: Long): List<User>

    /**
     * Получает список запросов в друзя от пользователя с переданным идентификатором.
     * @param userId идентификатор пользователя.
     * @return Возвращает список пользователей, которым была отправлена заявка в
     * друзья пользователю с переданным идентификатором.
     */
    fun getFriendRequestFromUser(userId: Long): List<User>

    /**
     * Получает список друзей пользователя с переданным идентификатором.
     * @param userId идентификатор пользователя.
     * @return Возвращает список друзей пользоваетля.
     */
    fun getFriendsOfUser(userId: Long): List<User>

    /**
     * Получает результат глобального поиска в виде, где по ключу FriendshipStatus.FRIEND находится список
     * друзей пользователя, а по ключу FriendshipStatus.NOT_FRIEND остальных пользователей.
     * @param userId идентификатор пользователя.
     * @return Возвращает вышеописанный Map.
     */
    fun getAllPeopleWithFriends(userId: Long): Map<FriendshipStatus, List<User>>

    /**
     * Отправляет от первого пользоваетля запрос на дружбу второму пользоваетелю.
     * @param userId1 идентификатор пользователя, отправляющего запрос.
     * @param userId2 идентификатор пользователя, которому отправляется запрос.
     */
    fun sendRequestFrom1To2(userId1: Long, userId2: Long)

    /**
     * Удаляет запрос на дружбу от первого пользоваетля второму пользоваетелю.
     * @param userId1 идентификатор пользователя, удаляющего запрос.
     * @param userId2 идентификатор пользователя, которому был отправлен запрос.
     */
    fun removeRequestFrom1(userId1: Long, userId2: Long)
}