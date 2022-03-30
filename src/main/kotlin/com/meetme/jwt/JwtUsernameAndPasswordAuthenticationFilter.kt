package com.meetme.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import com.meetme.util.Constants
import com.meetme.util.Constants.TOKEN_EXPIRATION_AFTER_DAYS
import com.meetme.util.Constants.TOKEN_PREFIX
import io.jsonwebtoken.Jwts
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.io.IOException
import java.time.LocalDate
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtUsernameAndPasswordAuthenticationFilter(
    private val myAuthenticationManager: AuthenticationManager
) : UsernamePasswordAuthenticationFilter() {

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        try {
            val authenticationRequest = ObjectMapper()
                .readValue(request.inputStream, UsernameAndPasswordAuthenticationRequest::class.java)

            val authentication: Authentication = UsernamePasswordAuthenticationToken(
                authenticationRequest.username,
                authenticationRequest.password
            )
            return myAuthenticationManager.authenticate(authentication)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    override fun successfulAuthentication(
        request: HttpServletRequest?,
        response: HttpServletResponse,
        chain: FilterChain?,
        authResult: Authentication
    ) {
        val token = Jwts.builder()
            .setSubject(authResult.name)
            .claim("authorities", authResult.authorities)
            .setIssuedAt(Date())
            .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(TOKEN_EXPIRATION_AFTER_DAYS)))
            .signWith(Constants.secretKey)
            .compact()

        response.addHeader("Authorization", "$TOKEN_PREFIX$token")
    }


}