package taskly.system.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import taskly.system.domain.Task
import taskly.system.domain.User
import taskly.system.service.UserService

/**
 * @author Alexandru Stoica
 * @version 1.0
 */

@RestController
@RequestMapping("/user")
class UserController {

    @Autowired
    private lateinit var userService: UserService

    @ResponseBody
    @PostMapping("/register")
    fun register(@RequestBody user: User): User? =
            userService.save(user)

    @ResponseBody
    @Secured("ROLE_USER")
    @PostMapping("/update")
    fun update(@AuthenticationPrincipal active: User, @RequestBody with: User): ResponseEntity<User> =
            if (active.id != with.id) ResponseEntity(HttpStatus.UNAUTHORIZED)
            else userService.save(with).let { ResponseEntity<User>(it, HttpStatus.ACCEPTED) }

    @ResponseBody
    @Secured("ROLE_USER")
    @DeleteMapping("/delete")
    fun delete(@AuthenticationPrincipal active: User): ResponseEntity<User> =
            userService.delete(active.id).let { ResponseEntity(HttpStatus.ACCEPTED) }

    @ResponseBody
    @Secured("ROLE_USER")
    @GetMapping("/tasks")
    fun getTasksFromUser(@AuthenticationPrincipal active: User): Set<Task> = active.tasks

    @ResponseBody
    @Secured("ROLE_USER")
    @GetMapping("/get/{name}")
    fun findUsersByUsername(@PathVariable("name") name: String): List<User> =
            userService.findUsersByUsername(name)

    @ResponseBody
    @Secured("ROLE_USER")
    @GetMapping("/get/{id}")
    fun findUserById(@PathVariable("id") id: Int): User =
            userService.findUserById(id)

}
