package com.meetme.iterest

import com.fasterxml.jackson.annotation.JsonIgnore
import com.meetme.group.Group
import com.meetme.meeting.Meeting
import javax.persistence.*

@Entity(name = "Interest")
data class Interest(
    @Id
    @SequenceGenerator(
        name = "interest_sequence",
        sequenceName = "interest_sequence",
        allocationSize = 1,
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "interest_sequence"
    )
    @Column(name = "interest_id")
    val id: Long = 0,

    @Column(
        name = "name",
        unique = true,
    )
    val name: String = "",
) {

    @JsonIgnore
    @ManyToMany(
        targetEntity = Meeting::class,
        mappedBy = "interests"
    )
    var meetings: Set<Meeting> = setOf()

    @JsonIgnore
    @ManyToMany(
        targetEntity = Group::class,
        mappedBy = "interests"
    )
    var groups: Set<Group> = setOf()

    override fun toString(): String {
        return "Interest(" +
            "id: $id, " +
            "name: $name" +
            ")"
    }
}