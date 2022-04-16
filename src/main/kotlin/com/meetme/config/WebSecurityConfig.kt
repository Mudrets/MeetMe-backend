package com.meetme.config

import com.meetme.jwt.JwtTokenVerifier
import com.meetme.jwt.JwtUsernameAndPasswordAuthenticationFilter
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy

/**
 * Класс для настройки доступа различных категорий пользователей
 * к endpoint-ам.
 */
@Configuration
@EnableWebSecurity
class WebSecurityConfig : WebSecurityConfigurerAdapter() {

    /**
     * Отвечает за настройку доступа к различным endpoint-ам для разных пользователей по протоколу HTTP
     */
    @Throws(Exception::class)
    override fun configure(httpSecurity: HttpSecurity) {
        httpSecurity
            .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .addFilter(JwtUsernameAndPasswordAuthenticationFilter(authenticationManager()))
            .addFilterAfter(JwtTokenVerifier(), JwtUsernameAndPasswordAuthenticationFilter::class.java)
            .authorizeRequests()
            .antMatchers("/api/v1/user/register", "/main.js", "/api/v1/websocket/**", "/", "/uploads/**")
                .permitAll()
            .anyRequest()
            .authenticated()
    }

    /**
     * Ответчает за настройку доступа к endpoint-ам по протоколу WebSocket
     */
    override fun configure(web: WebSecurity) {
        web.ignoring().antMatchers("/stomp/**")
    }
}