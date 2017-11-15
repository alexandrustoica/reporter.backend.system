package taskly.system.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import taskly.system.domain.Project
import taskly.system.domain.Task
import taskly.system.domain.User
import taskly.system.repository.UserRepository
import taskly.system.service.UserService

/**
 * @author Alexandru Stoica
 * @version 1.0
 */

@RestController
@RequestMapping("/user")
class UserController {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var userService: UserService

    @GetMapping("/create")
    fun create(): User = userRepository.findUserById(1) ?: User()

    @ResponseBody
    @PostMapping("/register")
    fun register(@RequestBody user: User): User? =
            userService.save(user)

    @ResponseBody
    @GetMapping("/delete")
    fun delete(@RequestBody user: User): User = TODO()

    @ResponseBody
    @GetMapping("/update")
    fun update(@RequestBody user: User,
               @RequestBody with: User): User = TODO()

    @ResponseBody
    @GetMapping("/delete/{id}")
    fun delete(@PathVariable("id") id: Int): User = TODO()

    @ResponseBody
    @GetMapping("/edit/{id}")
    fun update(@PathVariable("id") id: Int,
               @RequestBody with: User): User = TODO()

    @ResponseBody
    @GetMapping("/tasks/{id}")
    fun getTasksFromUser(@PathVariable("id") id: Int): List<Task> = TODO()

    @ResponseBody
    @GetMapping("/projects/{id}")
    fun getProjectsFromUser(@PathVariable("id") id: Int): List<Project> = TODO()

    @ResponseBody
    @GetMapping("/get/{name}")
    fun findUsersByUsername(@PathVariable("name") name: String): List<User> = TODO()

    @ResponseBody
    @GetMapping("/get/{id}")
    fun findUserById(@PathVariable("id") id: Int): User? = TODO()
}