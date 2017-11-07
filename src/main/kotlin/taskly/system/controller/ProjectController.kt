package taskly.system.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import taskly.system.domain.ProjectEntity
import taskly.system.domain.TaskEntity
import taskly.system.domain.UserEntity
import taskly.system.repository.ProjectRepository

/**
 * @author Alexandru Stoica
 * @version 1.0
 */

@RestController
@RequestMapping("/project")
class ProjectController {

    @Autowired
    lateinit var projectRepository: ProjectRepository

    @ResponseBody
    @RequestMapping("/insert")
    fun insert(@RequestBody project: ProjectEntity): ProjectEntity = TODO()

    @ResponseBody
    @RequestMapping("/delete")
    fun delete(@RequestBody project: ProjectEntity): ProjectEntity = TODO()

    @ResponseBody
    @RequestMapping("/update")
    fun update(@RequestBody project: ProjectEntity,
               @RequestBody with: ProjectEntity): ProjectEntity = TODO()

    @ResponseBody
    @RequestMapping("/delete/{id}")
    fun delete(@PathVariable("id") id: Int): ProjectEntity = TODO()

    @ResponseBody
    @RequestMapping("/update/{id}")
    fun update(@PathVariable("id") id: Int,
               @RequestBody with: ProjectEntity): ProjectEntity = TODO()

    @ResponseBody
    @RequestMapping("/tasks/{id}")
    fun getTasksFromProject(@PathVariable("id") id: Int): List<TaskEntity> = TODO()

    @ResponseBody
    @RequestMapping("/users/{id}")
    fun getUsersFromProject(@PathVariable("id") id: Int): List<UserEntity> = TODO()

    @ResponseBody
    @RequestMapping("/get/{name}")
    fun findProjectsByName(@PathVariable("name") name: String): List<ProjectEntity> = TODO()

    @ResponseBody
    @RequestMapping("/get/{id}")
    fun findProjectById(@PathVariable("id") id: Int): ProjectEntity? = TODO()
}