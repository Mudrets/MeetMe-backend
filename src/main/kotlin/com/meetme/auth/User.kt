package com.meetme.auth

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.*


@Entity
@Table(name = "users")
data class User(
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
        val id: Long = 0,

    @JvmField
        var username: String? = null,

    @JvmField
        var password: String? = null,
) : UserDetails {
        override fun getAuthorities(): MutableCollection<out GrantedAuthority>?  = null

        override fun getPassword(): String? = password

        override fun getUsername(): String? = username

        override fun isAccountNonExpired(): Boolean = true

        override fun isAccountNonLocked(): Boolean = true

        override fun isCredentialsNonExpired(): Boolean = true

        override fun isEnabled(): Boolean = true

}