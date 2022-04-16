package com.meetme.db.group

import com.meetme.db.interest.Interest
import com.meetme.db.meeting.Meeting
import com.meetme.db.user.User
import com.meetme.db.invitation.Invitation
import com.meetme.domain.entity.ParticipantsContainer
import javax.persistence.*

/**
 * Хранит данные о группе. Представляет из себя модель таблицы в базе данных.
 */
@Entity(name = "Groups")
class Group(
    /**
     * Идентификатор группы.
     */
    @Id
    @SequenceGenerator(
        name = "group_sequence",
        sequenceName = "group_sequence",
        allocationSize = 1,
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "group_sequence"
    )
    @Column(name = "group_id")
    val id: Long = 0,

    /**
     * Название группы.
     */
    @Column(name = "name")
    var name: String = "",

    /**
     * Описание группы.
     */
    @Column(name = "description")
    var description: String? = null,

    /**
     * Ссылка на аватрку группы.
     */
    @Column(name = "photo_url")
    var photoUrl: String? = null,

    /**
     * Флаг приватности группы.
     */
    @Column(name = "is_private")
    var isPrivate: Boolean = false,

    /**
     * Администратор группы.
     */
    @ManyToOne(targetEntity = User::class)
    val admin: User = User(),

    /**
     * Посты содержащие данные о мероприятии, на которое приглашена группа.
     */
    @OneToMany(targetEntity = Post::class, mappedBy = "group", cascade = [CascadeType.ALL])
    val posts: MutableSet<Post> = mutableSetOf(),

    /**
     * Участники группы.
     */
    @Column(name = "participants")
    @ManyToMany(targetEntity = User::class)
    @JoinTable(
        name = "participants_of_group",
        joinColumns = [JoinColumn(name = "group_id")],
        inverseJoinColumns = [JoinColumn(name = "user_id")],
    )
    override val participants: MutableList<User> = mutableListOf(admin),

    /**
     * Интересы группы.
     */
    @ManyToMany(targetEntity = Interest::class)
    @JoinTable(
        name = "interests_of_group",
        joinColumns = [JoinColumn(name = "group_id")],
        inverseJoinColumns = [JoinColumn(name = "interest_id")],
    )
    var interests: Set<Interest> = mutableSetOf(),

    /**
     * Приглашения для группы.
     */
    @ManyToMany(targetEntity = Invitation::class, mappedBy = "groups", cascade = [CascadeType.MERGE])
    val invitations: MutableList<Invitation> = mutableListOf()
) : ParticipantsContainer {

    /**
     * Проверяет содержит ли группа переданный митинг.
     * @param meeting наличие которого проверяется в группе.
     * @return Возвращает True если группа подтвердила свое участие в
     * переданном мероприятие, и False в противном случае.
     */
    fun containsMeeting(meeting: Meeting) =
        posts
            .map(Post::meeting)
            .contains(meeting)

    /**
     * Возвращает список мероприятий, на которых группа подтвердила свое участие.
     */
    val meetings: List<Meeting>
        get() = posts
            .map(Post::meeting)

    /**
     * Возвращает название и id группы в формате name:id.
     */
    val nameWithId: String
        get() = "$name:$id"

}