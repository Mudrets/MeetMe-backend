package com.meetme.jwt

import com.meetme.util.Constants
import com.meetme.util.Constants.TOKEN_PREFIX
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtTokenVerifier : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authorizationHeader = request.getHeader("Authorization")

        if (authorizationHeader.isNullOrEmpty() || !authorizationHeader.startsWith(TOKEN_PREFIX)) {
            filterChain.doFilter(request, response)
            return
        }
        val token = authorizationHeader.replace(TOKEN_PREFIX, "")

        try {
            val claimsJws = Jwts.parserBuilder()
                .setSigningKey(Constants.secretKey)
                .build()
                .parseClaimsJws(token)

            val body = claimsJws.body
            val username = body.subject
            val authorities = body["authorities"] as List<Map<String, String>>
            val simpleGrantedAuthorities = authorities.asSequence()
                .map { m -> SimpleGrantedAuthority(m["authority"]) }
                .toSet()
            val authentication: Authentication = UsernamePasswordAuthenticationToken(
                username,
                null,
                simpleGrantedAuthorities
            )

            SecurityContextHolder.getContext().authentication = authentication

        } catch (e: JwtException) {
            throw IllegalStateException("Token $token cannot be truest")
        }

        filterChain.doFilter(request, response)
    }
}