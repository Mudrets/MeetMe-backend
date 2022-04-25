package com.meetme.db.meeting

import com.fasterxml.jackson.annotation.JsonIgnore
import com.meetme.db.chat.Chat
import com.meetme.db.user.User
import com.meetme.db.image_store.Image
import com.meetme.db.interest.Interest
import com.meetme.db.invitation.Invitation
import com.meetme.db.group.Post
import com.meetme.domain.entity.ParticipantsContainer
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*
import javax.persistence.*

/**
 * Хранит данные по мероприятии. Представляет из себя модель таблицы в базе данных.
 */
@Entity(name = "Meeting")
data class Meeting(
    /**
     * Идентификатор.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meeting_name")
    val id: Long = 0,

    /**
     * Название мероприятия.
     */
    @Column(name = "name")
    var name: String = "",

    /**
     * Описание мероприятия.
     */
    @Column(name = "description")
    var description: String? = null,

    /**
     * Ссылка на фотографию мероприятия.
     */
    @Column(name = "photo_url")
    var photoUrl: String? = null,

    /**
     * Проходит ли мероприятие в онлайн формате.
     */
    @Column(name = "is_online")
    var isOnline: Boolean = false,

    /**
     * Дата и время начала мероприятия.
     */
    @Column(name = "start_date")
    var startDate: String = "",

    /**
     * Дата и время конца мероприятия.
     */
    @Column(name = "end_date")
    var endDate: String? = null,

    /**
     * Является ли мероприятие приватным.
     */
    @Column(name = "is_private")
    var isPrivate: Boolean = false,

    /**
     * Место проведения мероприятия.
     */
    @Column(name = "location")
    var location: String? = null,

    /**
     * Максимальное количество участников мероприятия.
     */
    @Column(name = "max_number_of_participants")
    var maxNumberOfParticipants: Int = 1,

    /**
     * Администратор мероприятия.
     */
    @ManyToOne(targetEntity = User::class)
    @JoinColumn(name = "user_id")
    val admin: User = User(),

    /**
     * Сет интересов мероприятия.
     */
    @ManyToMany(targetEntity = Interest::class)
    @JoinTable(
        name = "interests_of_meeting",
        joinColumns = [JoinColumn(name = "meeting_id")],
        inverseJoinColumns = [JoinColumn(name = "interest_id")],
    )
    var interests: MutableSet<Interest> = mutableSetOf(),

    /**
     * Список участников мероприятия.
     */
    @ManyToMany(targetEntity = User::class, cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinTable(
        name = "participants_of_meeting",
        joinColumns = [JoinColumn(name = "meeting_id")],
        inverseJoinColumns = [JoinColumn(name = "user_id")],
    )
    override val participants: MutableList<User> = mutableListOf(admin),

    /**
     * Чат мероприятия.
     */
    @OneToOne(
        targetEntity = Chat::class,
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
        fetch = FetchType.LAZY,
    )
    var chat: Chat = Chat(),

    /**
     * Сет связанных с мероприятияем записей.
     */
    @OneToMany(targetEntity = Post::class, mappedBy = "meeting", cascade = [CascadeType.ALL])
    val postsWithMeeting: MutableSet<Post> = mutableSetOf()

) : ParticipantsContainer {

    /**
     * Список картинок, хранящихся после проведения мероприятия.
     */
    @JsonIgnore
    @OneToMany(targetEntity = Image::class, mappedBy = "meeting")
    val images: MutableList<Image> = mutableListOf()

    /**
     * Приглашение на мероприятие.
     */
    @JsonIgnore
    @OneToOne(
        targetEntity = Invitation::class,
        mappedBy = "meeting",
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
        fetch = FetchType.LAZY,
    )
    val invitation: Invitation? = null

    /**
     * Количество участников мероприятия.
     */
    val numberOfParticipants: Int
        get() = participants.size

    /**
     * Проверяет является ли мероприятие посещенным.
     */
    val isVisitedMeeting: Boolean
        get() {
            val now = Date.from(Instant.now())
            val format = SimpleDateFormat("MM-dd-yyyy HH:mm")
            val dateStr = endDate ?: startDate
            return format.parse(dateStr).before(now)
        }
}
