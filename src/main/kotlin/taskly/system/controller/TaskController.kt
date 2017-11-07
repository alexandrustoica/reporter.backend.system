package taskly.system.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import taskly.system.domain.ProjectEntity
import taskly.system.domain.TaskEntity
import taskly.system.domain.UserEntity
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
    fun insert(@RequestBody task: TaskEntity): TaskEntity = TODO()

    @ResponseBody
    @RequestMapping("/delete")
    fun delete(@RequestBody task: TaskEntity): TaskEntity = TODO()

    @ResponseBody
    @RequestMapping("/update")
    fun update(@RequestBody task: TaskEntity,
               @RequestBody with: TaskEntity): TaskEntity = TODO()

    @ResponseBody
    @RequestMapping("/delete/{id}")
    fun delete(@PathVariable("id") id: Int): TaskEntity = TODO()

    @ResponseBody
    @RequestMapping("/update/{id}")
    fun update(@PathVariable("id") id: Int,
               @RequestBody task: TaskEntity): TaskEntity = TODO()

    @ResponseBody
    @RequestMapping("/projects/{id}")
    fun getProjectsFromTask(@PathVariable("id") id: Int): List<ProjectEntity> = TODO()

    @ResponseBody
    @RequestMapping("/users/{id}")
    fun getUsersFromTask(@PathVariable("id") id: Int): List<UserEntity> = TODO()

    @ResponseBody
    @RequestMapping("/get/{id}")
    fun findTaskById(@PathVariable("id") id: Int): TaskEntity? = TODO()

    @ResponseBody
    @RequestMapping("/get/{location}")
    fun findTasksByLocation(@PathVariable("location") location: String): List<TaskEntity> = TODO()
}