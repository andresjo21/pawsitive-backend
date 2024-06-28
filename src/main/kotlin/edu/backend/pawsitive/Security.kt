package edu.backend.pawsitive

import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.*
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import java.io.IOException
import java.security.Key
import java.util.*

object SecurityConst {
    //JWT token defaults val TOKEN_PREFIX = "Bearer "
    const val TOKEN_TYPE = "JWT"
    const val TOKEN_ISSUER = "secure-api"
    const val TOKEN_AUDIENCE = "secure-app"
    const val TOKEN_LIFETIME: Long = 864000000
    const val TOKEN_PREFIX = "Bearer "
    const val APPLICATION_JSON = "application/json"
    const val UTF_8 = "UTF-8"
    val TOKEN_SECRET: String =
        "=========================================================pawsitiveApp=========================================================pawsitiveApp========================================================="
}

class JwtAuthenticationFilter(authenticationManager: AuthenticationManager) : UsernamePasswordAuthenticationFilter() {

    private val authManager: AuthenticationManager

    init {
        setFilterProcessesUrl("/v1/users/login")
        authManager = authenticationManager
    }

    @Throws(AuthenticationException::class)
    override fun attemptAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
    ): Authentication {

        if (request.method != "POST") {
            throw AuthenticationServiceException("Authentication method not supported: $request.method")
        }

        return try {
            val userLoginInput: UserLoginInput = ObjectMapper()
                .readValue(request.inputStream, UserLoginInput::class.java)
            authManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    userLoginInput.username,
                    userLoginInput.password,
                    ArrayList()
                )
            )
        } catch (exception: IOException) {
            throw RuntimeException(exception)
        }
    }

    override fun successfulAuthentication(
        request: HttpServletRequest, response: HttpServletResponse,
        filterchain: FilterChain, authentication: Authentication,
    ) {

        val objectMapper = ObjectMapper()

        val token = Jwts.builder()
            .signWith(key(), SignatureAlgorithm.HS512)
            .setHeaderParam("typ", SecurityConst.TOKEN_TYPE)
            .setIssuer(SecurityConst.TOKEN_ISSUER)
            .setAudience(SecurityConst.TOKEN_AUDIENCE)
            .setSubject((authentication.principal as org.springframework.security.core.userdetails.User).username)
            .setExpiration(Date(System.currentTimeMillis() + SecurityConst.TOKEN_LIFETIME))
            .compact()

        response.addHeader(HttpHeaders.AUTHORIZATION, SecurityConst.TOKEN_PREFIX + token)
        val out = response.writer
        response.contentType = SecurityConst.APPLICATION_JSON
        response.characterEncoding = SecurityConst.UTF_8
        out.print(objectMapper.writeValueAsString(authentication.principal))
        out.flush()
    }
}

/**
 * This function will return the key to sign the token
 */
private fun key(): Key {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(SecurityConst.TOKEN_SECRET))
}

/**
 * This class will validate the token
 */
class JwtAuthorizationFilter(authenticationManager: AuthenticationManager) :
    BasicAuthenticationFilter(authenticationManager) {

    @Throws(IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest, response: HttpServletResponse,
        filterChain: FilterChain,
    ) {

        var authorizationToken = request.getHeader(HttpHeaders.AUTHORIZATION)

        if (authorizationToken != null && authorizationToken.startsWith(SecurityConst.TOKEN_PREFIX)) {
            authorizationToken = authorizationToken.replaceFirst(SecurityConst.TOKEN_PREFIX.toRegex(), "")
            val username: String =
                Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(authorizationToken).body.subject

            LoggedUser.logIn(username)

            SecurityContextHolder.getContext().authentication =
                UsernamePasswordAuthenticationToken(username, null, emptyList())
        }

        filterChain.doFilter(request, response)
    }

}

/**
 * Object to holder the user information
 */
object LoggedUser {
    private val userHolder = ThreadLocal<String>()
    fun logIn(user: String) {
        userHolder.set(user)
    }

    fun logOut() {
        // if there's no user, this will throw an exception
        userHolder.remove()
    }

    fun get(): String {
        return userHolder.get()
    }
}