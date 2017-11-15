package taskly.system.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.web.bind.annotation.*
import taskly.system.domain.Project
import taskly.system.domain.Task
import taskly.system.domain.User
import taskly.system.exception.DataNotFound
import taskly.system.repository.ProjectRepository
import javax.servlet.http.HttpServletRequest

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
    @PostMapping("/insert")
    fun insert(@RequestBody project: Project): Project =
            projectRepository.save(project)

    @ResponseBody
    @RequestMapping("/insert/{name}")
    fun insert(@PathVariable("name") name: String): Project =
            projectRepository.save(Project(name))

    @ResponseBody
    @PostMapping("/update/{id}")
    fun update(@PathVariable id: Int,
               @RequestBody with: Project): Project? =
            projectRepository.updateByName(id, with.name)
                    .let { projectRepository.findProjectById(id) }

    @RequestMapping("/delete/{id}")
    fun delete(@PathVariable("id") id: Int) =
            projectRepository.delete(id)

    @ResponseBody
    @RequestMapping("/tasks/{id}")
    fun getTasksFromProject(@PathVariable("id") id: Int): List<Task> =
            projectRepository.findProjectById(id)?.tasks?.toList() ?: listOf()

    @ResponseBody
    @RequestMapping("/users/{id}")
    fun getUsersFromProject(@PathVariable("id") id: Int): List<User> = TODO()

    @ResponseBody
    @RequestMapping("/get/name/{name}")
    fun findProjectsByName(@PathVariable("name") name: String): List<Project> =
            projectRepository.findProjectsByName(name)

    @ResponseBody
    @RequestMapping("/get/id/{id}")
    fun findProjectById(@PathVariable("id") id: Int): Project? =
            projectRepository.findProjectById(id)

    @ResponseBody
    @ExceptionHandler(EmptyResultDataAccessException::class)
    fun handlerDataAccessException(request: HttpServletRequest,
                                   exception: EmptyResultDataAccessException) =
            DataNotFound(exception.message, errorCode = 404)
}