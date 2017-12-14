package taskly.system.user

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

/**
 * @author Alexandru Stoica
 * @version 1.0
 */

@RestController
@RequestMapping("/users")
class UserController {

    @Autowired
    private lateinit var userService: UserService

    @ResponseBody
    @PostMapping("/register")
    fun register(@RequestBody user: User): User? =
            userService.save(user)
}
