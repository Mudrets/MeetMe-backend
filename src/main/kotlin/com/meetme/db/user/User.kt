package com.meetme.db.user

import com.fasterxml.jackson.annotation.JsonIgnore
import com.meetme.db.media_link.MediaLink
import com.meetme.db.group.Group
import com.meetme.db.interest.Interest
import com.meetme.db.invitation.Invitation
import com.meetme.db.meeting.Meeting
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.*

/**
 * Хранит данные по пользователе. Представляет из себя модель таблицы в базе данных.
 */
@Entity(name = "users")
data class User(
    /**
     * Идентификатор.
     */
    @Id
    @SequenceGenerator(
        name = "user_sequence",
        sequenceName = "user_sequence",
        allocationSize = 1,
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "user_sequence"
    )
    @Column(name = "user_id")
    val id: Long = 0,

    /**
     * Имя пользователя.
     */
    @Column(name = "name")
    var name: String = "",

    /**
     * Фамилия пользователя.
     */
    @Column(name = "surname")
    var surname: String = "",

    /**
     * Почта пользователя.
     */
    @Column(unique = true)
    var email: String? = null,

    /**
     * Телефон пользователя.
     */
    @Column(unique = true)
    var telephone: String? = null,

    /**
     * Зашифрованный пароль пользователя.
     */
    @JvmField
    var password: String? = null,

    /**
     * Описание профиля пользователя.
     */
    @Column(name = "description")
    var description: String? = null,

    /**
     * Ссылка на фотографию пользователя.
     */
    @Column(name = "photo_url")
    var photoUrl: String = "",


) : UserDetails {

    /**
     * Администрируемые пользователем мероприятия.
     */
    @JsonIgnore
    @OneToMany(mappedBy = "admin", cascade = [CascadeType.REMOVE])
    var managedMeetings: MutableSet<Meeting> = mutableSetOf()

    /**
     * Мероприятия, в которых пользователь принимает участие.
     */
    @JsonIgnore
    @ManyToMany(
        targetEntity = Meeting::class,
        cascade = [CascadeType.PERSIST, CascadeType.MERGE],
        mappedBy = "participants"
    )
    var meetings: MutableSet<Meeting> = mutableSetOf()

    /**
     * Администруремые пользователем группы.
     */
    @JsonIgnore
    @OneToMany(mappedBy = "admin", cascade = [CascadeType.REMOVE])
    var managedGroups: MutableSet<Group> = mutableSetOf()

    /**
     * Группы, в которых пользователь прнимает участие.
     */
    @JsonIgnore
    @ManyToMany(
        targetEntity = Group::class,
        cascade = [CascadeType.PERSIST, CascadeType.MERGE],
        mappedBy = "participants"
    )
    var groups: MutableSet<Group> = mutableSetOf()

    /**
     * Сет интересов пользователя.
     */
    @ManyToMany(targetEntity = Interest::class)
    var interests: Set<Interest> = mutableSetOf()

    /**
     * Сет ссылок на социальные сети пользователя.
     */
    @OneToMany(targetEntity = MediaLink::class, mappedBy = "user")
    var socialMediaLinks: Set<MediaLink> = mutableSetOf()

    /**
     * Список приглашений пользователя на мероприятия.
     */
    @ManyToMany(targetEntity = Invitation::class, mappedBy = "users")
    val invitations: MutableList<Invitation> = mutableListOf()

    /**
     * Сет всех мероприятий к которым пользователь имеет какое-то отношение.
     */
    val allMeetings: Set<Meeting>
        get() = meetings.union(managedMeetings)

    override fun getAuthorities(): MutableCollection<out GrantedAuthority>? = null

    override fun getPassword(): String? = password

    override fun getUsername(): String? = email

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true

    /**
     * Возвращает полное имя пользователя.
     */
    val fullName: String
        get() = "$name${if (surname.isNotBlank()) " $surname" else ""}"

    /**
     * Возвращает имя и id пользователя в виде name:id.
     */
    val nameWithId: String
        get() = "$name:$id"
}