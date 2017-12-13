package taskly.system.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import taskly.system.domain.Task
import taskly.system.domain.User
import taskly.system.exception.TaskNotFoundException
import taskly.system.repository.TaskRepository

@Service
class TaskService {

    @Autowired
    private lateinit var taskRepository: TaskRepository

    fun save(task: Task): Task? = taskRepository.save(task)

    fun getTasksForUser(user: User, page: Pageable): Page<Task> =
            taskRepository.findByUsersContaining(user, page)

    @Throws(TaskNotFoundException::class)
    fun findTaskById(id: Int) =
            taskRepository.findTaskById(id) ?: throw TaskNotFoundException()
}
