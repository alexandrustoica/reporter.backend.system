package taskly.system.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import taskly.system.domain.Task
import taskly.system.exception.TaskNotFoundException
import taskly.system.exception.UnableToSaveTaskException
import taskly.system.repository.TaskRepository
import taskly.system.security.SecurityConstants

@Service
class TaskService {

    @Autowired
    private lateinit var taskRepository: TaskRepository

    @Autowired
    private lateinit var securityConstants: SecurityConstants


    fun save(task: Task) = taskRepository.save(task) ?:
            throw UnableToSaveTaskException()

    fun delete(id: Int) =
            findTaskById(id).let { taskRepository.delete(it) }

    fun findTasksByLocation(location: String) =
            taskRepository.findTasksByLocation(location)

    @Throws(TaskNotFoundException::class)
    fun findTaskById(id: Int) =
            taskRepository.findTaskById(id) ?: throw TaskNotFoundException()
}
