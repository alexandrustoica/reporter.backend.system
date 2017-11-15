package taskly.system.security

import io.jsonwebtoken.Jwts
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import taskly.system.repository.UserRepository
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @author Alexandru Stoica
 * @version 1.0
 */

class JsonWebTokenAuthorizationFilter(private val manager: AuthenticationManager,
                                      private val constants: SecurityConstants,
                                      private val userRepository: UserRepository) :
        BasicAuthenticationFilter(manager) {

    override fun doFilterInternal(request: HttpServletRequest,
                                  response: HttpServletResponse,
                                  chain: FilterChain) {
        val header = response.getHeader(constants.HEADER_STRING) ?: ""
        if (header == "" || !header.startsWith(constants.TOKEN_PREFIX)) {
            chain.doFilter(request, response)
            return
        }
        val authentication = getAuthenticationFromRequest(request)
        SecurityContextHolder.getContext().authentication = authentication
        chain.doFilter(request, response)
    }

    private fun getAuthenticationFromRequest(request: HttpServletRequest): UsernamePasswordAuthenticationToken? {
        val token = request.getHeader(constants.HEADER_STRING) ?: ""
        val username = Jwts.parser()
                .setSigningKey(constants.SECRET.toByteArray())
                .parseClaimsJws(token.replace(constants.TOKEN_PREFIX, ""))
                .body.subject ?: ""
        return if (username != "") userRepository.findByUsername(username)
                .let { UsernamePasswordAuthenticationToken(it, it?.password, it?.authorities) }
        else null
    }
}
