package taskly.system.user

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import taskly.system.security.SecurityConstants
import java.util.*


@Service
class UserService : UserDetailsService {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var securityConstants: SecurityConstants

    fun save(user: User): User? = userRepository.
            save(user.copy(password = BCryptPasswordEncoder().encode(user.password)))

    @Throws(UserNotFound::class)
    override fun loadUserByUsername(username: String): UserDetails =
            userRepository.findByUsername(username) ?:
                    throw UserNotFound()

    fun getJsonWebToken(user: User): String = Jwts.builder()
            .setSubject(user.username)
            .setExpiration(Date(System.currentTimeMillis() + securityConstants.EXPIRATION_TIME))
            .signWith(SignatureAlgorithm.HS512, securityConstants.SECRET.toByteArray())
            .compact()
}
