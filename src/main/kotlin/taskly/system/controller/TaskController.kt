package taskly.system.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import springfox.documentation.annotations.ApiIgnore
import taskly.system.domain.Task
import taskly.system.domain.User
import taskly.system.service.TaskService
import taskly.system.service.UserService

/**
 * @author Alexandru Stoica
 * @version 1.0
 */

@RestController
@RequestMapping("/tasks")
class TaskController {

    @Autowired
    private lateinit var taskService: TaskService

    @Autowired
    private lateinit var userService: UserService

    @ResponseBody
    @Secured("ROLE_USER")
    @PostMapping("")
    fun insert(@AuthenticationPrincipal @ApiIgnore user: User,
               @RequestBody task: Task): ResponseEntity<Task?> =
            ResponseEntity(saveTaskToDatabase(task, user))

    @ResponseBody
    @Secured("ROLE_USER")
    @GetMapping("")
    fun all(@AuthenticationPrincipal @ApiIgnore user: User,
            @RequestParam(required = true) page: Int,
            @RequestParam(required = true) limit: Int) =
            taskService.getTasksForUser(getUserFromDatabase(user), PageRequest(page, limit))

    @ResponseBody
    @Secured("ROLE_USER")
    @PutMapping("")
    fun update(@AuthenticationPrincipal @ApiIgnore user: User,
               @RequestBody task: Task): HttpStatus =
            if (user.id in taskService.findTaskById(task.id).users.map { it.id })
                taskService.save(task).let { HttpStatus.ACCEPTED }
            else HttpStatus.UNAUTHORIZED

    private fun saveTaskToDatabase(task: Task, user: User) =
            if (taskService.save(task.pushUser(getUserFromDatabase(user))) != null)
                HttpStatus.ACCEPTED else HttpStatus.UNAUTHORIZED

    private fun getUserFromDatabase(user: User) =
            userService.findUserById(user.id)
}
