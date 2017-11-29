package taskly.system.security

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import taskly.system.domain.User
import taskly.system.service.UserService
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @author Alexandru Stoica
 * @version 1.0
 */

class JsonWebTokenAuthenticationFilter(private val manager: AuthenticationManager,
                                       private val constants: SecurityConstants,
                                       private val userService: UserService) : UsernamePasswordAuthenticationFilter() {

    @Throws(AuthenticationException::class)
    override fun attemptAuthentication(request: HttpServletRequest,
                                       response: HttpServletResponse?): Authentication {
        val credentials = ObjectMapper().readValue(request.inputStream, AccountCredentials::class.java)
        return manager.authenticate(UsernamePasswordAuthenticationToken(credentials.username, credentials.password, mutableListOf()))
    }

    @Throws(IOException::class, ServletException::class)
    override fun successfulAuthentication(request: HttpServletRequest,
                                          response: HttpServletResponse,
                                          filterChain: FilterChain,
                                          authentication: Authentication) {
        val token = userService.getJsonWebToken(authentication.principal as User)
        response.addHeader(constants.HEADER_STRING, constants.TOKEN_PREFIX + token)
    }
}
