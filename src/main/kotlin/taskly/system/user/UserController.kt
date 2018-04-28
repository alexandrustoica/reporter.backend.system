package taskly.system.user

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import springfox.documentation.annotations.ApiIgnore


@RestController
@RequestMapping("/users")
class UserController {

    @Autowired
    private lateinit var userService: UserService

    @ResponseBody
    @PostMapping("/register")
    fun register(@RequestBody user: User): ResponseEntity<User> =
            userService.save(user)
                    ?.let { ResponseEntity(it, HttpStatus.ACCEPTED) }
                    ?: ResponseEntity(HttpStatus.NOT_ACCEPTABLE)

    @ResponseBody
    @PostMapping("/police")
    fun registerAsPoliceMan(@RequestBody user: User): ResponseEntity<User> =
            userService.save(user.copy(role = UserRole.POLICE))?.let {
                ResponseEntity(it, HttpStatus.ACCEPTED)
            } ?: ResponseEntity(HttpStatus.NOT_ACCEPTABLE)

    @ResponseBody
    @DeleteMapping("/")
    @Secured(value = ["ROLE_USER", "ROLE_POLICE"])
    fun delete(@AuthenticationPrincipal @ApiIgnore user: User): ResponseEntity<User> =
            userService.delete(user.id)
                    ?.let { ResponseEntity(it, HttpStatus.ACCEPTED) }
                    ?: ResponseEntity(HttpStatus.NOT_ACCEPTABLE)

    @ResponseBody
    @GetMapping("/current")
    @Secured(value = ["ROLE_USER", "ROLE_POLICE"])
    fun getCurrentLoggedUser(
            @ApiIgnore @AuthenticationPrincipal user: User): ResponseEntity<User> =
            userService.getById(user.id)
                    ?.let { ResponseEntity(it, HttpStatus.ACCEPTED) }
                    ?: ResponseEntity(HttpStatus.NOT_FOUND)

    @ResponseBody
    @PutMapping("/")
    @Secured(value = ["ROLE_USER", "ROLE_POLICE"])
    fun update(@RequestBody updated: User,
               @ApiIgnore @AuthenticationPrincipal user: User): ResponseEntity<User> =
            if (user.id == updated.id) userService.update(updated)
                    ?.let { ResponseEntity(it, HttpStatus.ACCEPTED) }
                    ?: ResponseEntity(HttpStatus.NOT_FOUND)
            else ResponseEntity(HttpStatus.NOT_ACCEPTABLE)
}
