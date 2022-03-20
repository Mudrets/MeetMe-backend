package com.meetme.group.post

import com.fasterxml.jackson.annotation.JsonIgnore
import com.meetme.group.Group
import javax.persistence.*

@Entity(name = "Post")
data class Post(
    @Id
    @SequenceGenerator(
        name = "post_sequence",
        sequenceName = "post_sequence",
        allocationSize = 1,
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "post_sequence"
    )
    @Column(name = "post_id")
    val id: Long = 0,

    @Column(name = "title")
    val title: String = "",

    @Column(name = "text")
    val text: String = "",

    @JsonIgnore
    @ManyToOne(targetEntity = Group::class)
    @JoinColumn(name = "posts")
    val group: Group? = null,
)
