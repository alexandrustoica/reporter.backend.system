package taskly.system.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import taskly.system.domain.Project
import taskly.system.domain.Task
import taskly.system.domain.User
import taskly.system.repository.TaskRepository

/**
 * @author Alexandru Stoica
 * @version 1.0
 */

@RestController
@RequestMapping("/task")
class TaskController {

    @Autowired
    lateinit var taskRepository: TaskRepository

    @ResponseBody
    @RequestMapping("/insert")
    fun insert(@RequestBody task: Task): Task = TODO()

    @ResponseBody
    @RequestMapping("/delete")
    fun delete(@RequestBody task: Task): Task = TODO()

    @ResponseBody
    @RequestMapping("/update")
    fun update(@RequestBody task: Task,
               @RequestBody with: Task): Task = TODO()

    @ResponseBody
    @RequestMapping("/delete/{id}")
    fun delete(@PathVariable("id") id: Int): Task = TODO()

    @ResponseBody
    @RequestMapping("/update/{id}")
    fun update(@PathVariable("id") id: Int,
               @RequestBody task: Task): Task = TODO()

    @ResponseBody
    @RequestMapping("/projects/{id}")
    fun getProjectsFromTask(@PathVariable("id") id: Int): List<Project> = TODO()

    @ResponseBody
    @RequestMapping("/users/{id}")
    fun getUsersFromTask(@PathVariable("id") id: Int): List<User> = TODO()

    @ResponseBody
    @RequestMapping("/get/{id}")
    fun findTaskById(@PathVariable("id") id: Int): Task? = TODO()

    @ResponseBody
    @RequestMapping("/get/{location}")
    fun findTasksByLocation(@PathVariable("location") location: String): List<Task> = TODO()
}