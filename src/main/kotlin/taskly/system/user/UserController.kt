package taskly.system.user

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import springfox.documentation.annotations.ApiIgnore

/**
 * @author Alexandru Stoica
 * @version 1.0
 */

@RestController
@RequestMapping("/users")
class UserController {

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var userRepository: UserRepository

    @ResponseBody
    @PostMapping("/register")
    fun register(@RequestBody user: User): User? =
            userService.save(user)

    @ResponseBody
    @GetMapping("/current")
    fun getCurrentLoggedUser(@ApiIgnore @AuthenticationPrincipal user: User): User =
            userRepository.findUserById(user.id) ?: throw UserNotFound()
}
