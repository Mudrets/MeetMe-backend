package com.meetme.service.friends

import com.meetme.util.doIfExist
import com.meetme.db.user.User
import com.meetme.db.friends.Friendship
import com.meetme.db.friends.FriendshipDao
import com.meetme.service.friends.enums.FriendshipStatus
import com.meetme.service.user.UserServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Реализация сервиса для работы с заявками в друзья.
 */
@Service
class FriendshipServiceImpl @Autowired constructor(
    /**
     * Data access object для взаимодействия с таблицей запросов в друзья в базе данных.
     */
    private val friendshipDao: FriendshipDao,
    /**
     * Сервис для работы с данными пользователя.
     */
    private val userService: UserServiceImpl,
) : FriendshipService {
    /**
     * Получает сущность дружеских отношений по двум пользователям.
     * @param user1 первый пользователь.
     * @param user2 второй пользователь.
     * @return Возвращает сущность дружеских отношений между пользователями.
     */
    private fun getFriendship(user1: User, user2: User): Friendship? =
        friendshipDao.findByUser1AndUser2(user1, user2)
            ?: friendshipDao.findByUser1AndUser2(user2, user1)

    /**
     * Получает сущность дружеских отношений по двум пользователям для удаления.
     * @param user1 первый пользователь.
     * @param user2 второй пользователь.
     * @return Возвращает сущность дружеских отношений между пользователями.
     */
    private fun getFriendshipForRemove(user1: User, user2: User) =
        getFriendship(user1, user2)
            ?: throw IllegalArgumentException(
                "Friendship between user with id = ${user1.id} and user with id = ${user2.id} does not exist"
            )

    /**
     * Получает список всех дружеских отношений с другими пользователями, отфильтрованный по
     * переданнму фильтру.
     * @param user пользователь.
     * @param filter фильтр.
     * @return Возвращает отфильтрованный список дружеских отношений пользователя.
     */
    private fun getFriendsFilterBy(user: User, filter: (Friendship) -> Boolean): List<User> =
        friendshipDao.findAllByUser1OrUser2(user, user).asSequence()
            .filter(filter)
            .map { friendship -> friendship.getFriend(user) }
            .toList()

    /**
     * Создает сузность дружеских отношений между пользователями.
     * @param user1 первый пользователь.
     * @param user2 второй пользователь.
     * @return Возвращает созданную сущность.
     */
    private fun createFriendship(user1: User, user2: User) =
        friendshipDao.save(
            Friendship(
                user1 = user1,
                user2 = user2,
                acceptFromUser1 = true
            )
        )

    /**
     * Получает всех друзей пользователя.
     * @param user пользователь.
     * @return Возвращает список друзей пользователя.
     */
    private fun getAllFriends(user: User): List<User> =
        friendshipDao.findAllByUser1OrUser2(user, user)
            .filter(Friendship::isFriendship)
            .map { friendship -> friendship.getFriend(user) }

    /**
     * Получает список заявок в друзья пользователю с переданным идентификатором.
     * @param userId идентификатор пользователя.
     * @return Возвращает список пользователей, от которых была отпралвена заявка в
     * друзья пользователю с переданным идентификатором.
     */
    override fun getFriendRequestToUser(userId: Long): List<User> =
        userId.doIfExist(userService) { user ->
            getFriendsFilterBy(user) { friendship -> friendship.isFriendRequestToUser(user) }
        }

    /**
     * Получает список запросов в друзя от пользователя с переданным идентификатором.
     * @param userId идентификатор пользователя.
     * @return Возвращает список пользователей, которым была отправлена заявка в
     * друзья пользователю с переданным идентификатором.
     */
    override fun getFriendRequestFromUser(userId: Long): List<User> =
        userId.doIfExist(userService) { user ->
            getFriendsFilterBy(user) { friendship -> friendship.isFriendRequestFromUser(user) }
        }

    /**
     * Получает список друзей пользователя с переданным идентификатором.
     * @param userId идентификатор пользователя.
     * @return Возвращает список друзей пользоваетля.
     */
    override fun getFriendsOfUser(userId: Long): List<User> =
        userId.doIfExist(userService) { user -> getAllFriends(user) }

    /**
     * Получает результат глобального поиска в виде, где по ключу FriendshipStatus.FRIEND находится список
     * друзей пользователя, а по ключу FriendshipStatus.NOT_FRIEND остальных пользователей.
     * @param userId идентификатор пользователя.
     * @return Возвращает вышеописанный Map.
     */
    override fun getAllPeopleWithFriends(userId: Long): Map<FriendshipStatus, List<User>> =
        userId.doIfExist(userService) { user ->
            val allUsers = userService.getAll().subtract(listOf(user).toSet())
            val friends = getAllFriends(user)
            val resMap = mapOf(
                FriendshipStatus.FRIEND to mutableListOf<User>(),
                FriendshipStatus.NOT_FRIEND to mutableListOf<User>()
            )
            allUsers.forEach { globalUser ->
                    if (friends.contains(globalUser))
                        resMap[FriendshipStatus.FRIEND]?.add(globalUser)
                    else
                        resMap[FriendshipStatus.NOT_FRIEND]?.add(globalUser)
                }
            resMap
        }

    /**
     * Отправляет от первого пользоваетля запрос на дружбу второму пользоваетелю.
     * @param userId1 идентификатор пользователя, отправляющего запрос.
     * @param userId2 идентификатор пользователя, которому отправляется запрос.
     */
    override fun sendRequestFrom1To2(userId1: Long, userId2: Long) {
        (userId1 to userId2).doIfExist(userService) { user1, user2 ->
            val friendship = getFriendship(user1, user2)
            if (friendship == null) {
                createFriendship(user1, user2)
            } else {
                friendship.setAcceptFrom(user1)
                friendshipDao.save(friendship)
            }
        }
    }

    /**
     * Удаляет запрос на дружбу от первого пользоваетля второму пользоваетелю.
     * @param userId1 идентификатор пользователя, удаляющего запрос.
     * @param userId2 идентификатор пользователя, которому был отправлен запрос.
     */
    override fun removeRequestFrom1(userId1: Long, userId2: Long) {
        (userId1 to userId2).doIfExist(userService) { user1, user2 ->
            val friendship = getFriendshipForRemove(user1, user2)
            friendship.cancelAcceptFor(user1)
            if (friendship.canceledFriendship)
                friendshipDao.delete(friendship)
            else
                friendshipDao.save(friendship)
        }
    }

}