package taskly.system.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import taskly.system.domain.ProjectEntity
import taskly.system.domain.TaskEntity
import taskly.system.domain.UserEntity
import taskly.system.repository.UserRepository

/**
 * @author Alexandru Stoica
 * @version 1.0
 */

@RestController
@RequestMapping("/user")
class UserController {

    @Autowired
    lateinit var userRepository: UserRepository

    @GetMapping("/create")
    fun create(): UserEntity = userRepository.findUserById(1) ?: UserEntity()

    @ResponseBody
    @GetMapping("/register")
    fun register(@RequestBody user: UserEntity): UserEntity? = TODO("")

    @ResponseBody
    @GetMapping("/login")
    fun login(@RequestBody user: UserEntity): UserEntity? = TODO("")

    @ResponseBody
    @GetMapping("/delete")
    fun delete(@RequestBody user: UserEntity): UserEntity = TODO()

    @ResponseBody
    @GetMapping("/update")
    fun update(@RequestBody user: UserEntity,
               @RequestBody with: UserEntity): UserEntity = TODO()

    @ResponseBody
    @GetMapping("/delete/{id}")
    fun delete(@PathVariable("id") id: Int): UserEntity = TODO()

    @ResponseBody
    @GetMapping("/edit/{id}")
    fun update(@PathVariable("id") id: Int,
               @RequestBody with: UserEntity): UserEntity = TODO()

    @ResponseBody
    @GetMapping("/tasks/{id}")
    fun getTasksFromUser(@PathVariable("id") id: Int): List<TaskEntity> = TODO()

    @ResponseBody
    @GetMapping("/projects/{id}")
    fun getProjectsFromUser(@PathVariable("id") id: Int): List<ProjectEntity> = TODO()

    @ResponseBody
    @GetMapping("/get/{name}")
    fun findUsersByUsername(@PathVariable("name") name: String): List<UserEntity> = TODO()

    @ResponseBody
    @GetMapping("/get/{id}")
    fun findUserById(@PathVariable("id") id: Int): UserEntity? = TODO()
}