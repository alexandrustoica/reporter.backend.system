package taskly.system.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import taskly.system.domain.Task
import taskly.system.domain.User
import taskly.system.service.TaskService
import taskly.system.service.UserService

/**
 * @author Alexandru Stoica
 * @version 1.0
 */

@RestController
@RequestMapping("/task")
class TaskController {

    @Autowired
    private lateinit var taskService: TaskService

    @Autowired
    private lateinit var userService: UserService

    @ResponseBody
    @Secured("ROLE_USER")
    @PostMapping("/insert")
    fun insert(@AuthenticationPrincipal user: User,
               @RequestBody task: Task): Set<Task> =
            user.addTask(task).let { userService.save(user)?.tasks } ?: setOf()

    @ResponseBody
    @Secured("ROLE_USER")
    @PostMapping("/update")
    fun update(@AuthenticationPrincipal user: User,
               @RequestBody task: Task): ResponseEntity<Task> =
            if (user.tasks.contains(task)) taskService.save(task).let { ResponseEntity<Task>(it, HttpStatus.ACCEPTED) }
            else ResponseEntity(HttpStatus.UNAUTHORIZED)

    @ResponseBody
    @Secured("ROLE_USER")
    @RequestMapping("/delete/{id}")
    fun delete(@AuthenticationPrincipal user: User,
               @PathVariable("id") id: Int): ResponseEntity<Task> =
            if (user.tasks.none { it.id == id }) ResponseEntity(HttpStatus.UNAUTHORIZED)
            else taskService.delete(id).let { ResponseEntity<Task>(HttpStatus.ACCEPTED) }

    @ResponseBody
    @Secured("ROLE_USER")
    @RequestMapping("/users/{id}")
    fun getUsersFromTask(@PathVariable("id") id: Int): Set<User> =
            taskService.findTaskById(id).users

    @ResponseBody
    @Secured("ROLE_USER")
    @RequestMapping("/get/{id}")
    fun findTaskById(@PathVariable("id") id: Int): Task? =
            taskService.findTaskById(id)

    @ResponseBody
    @Secured("ROLE_USER")
    @RequestMapping("/get/{location}")
    fun findTasksByLocation(@PathVariable("location") location: String): List<Task> =
            taskService.findTasksByLocation(location)

}
