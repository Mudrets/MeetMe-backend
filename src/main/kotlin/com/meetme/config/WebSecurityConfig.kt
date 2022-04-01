package com.meetme.config

import com.meetme.jwt.JwtTokenVerifier
import com.meetme.jwt.JwtUsernameAndPasswordAuthenticationFilter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import javax.crypto.SecretKey

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
            .antMatchers("/api/v1/user/register").permitAll()
            .antMatchers("/uploads/**").permitAll()
            .anyRequest()
            .authenticated()
    }
}