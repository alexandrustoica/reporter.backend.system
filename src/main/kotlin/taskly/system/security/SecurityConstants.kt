package taskly.system.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class SecurityConstants {
    @Value("\${app.security.secret}")
    val SECRET = "ThisIsASecret"
    val EXPIRATION_TIME: Long = 864000000
    val TOKEN_PREFIX = "Bearer "
    val HEADER_STRING = "Authorization"
}