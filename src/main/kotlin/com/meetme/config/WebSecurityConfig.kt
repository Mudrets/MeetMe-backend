package com.meetme.config

import com.meetme.jwt.JwtTokenVerifier
import com.meetme.jwt.JwtUsernameAndPasswordAuthenticationFilter
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy


@Configuration
@EnableWebSecurity
class WebSecurityConfig : WebSecurityConfigurerAdapter() {

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

    override fun configure(web: WebSecurity) {
        web.ignoring().antMatchers("/stomp/**")
    }
}