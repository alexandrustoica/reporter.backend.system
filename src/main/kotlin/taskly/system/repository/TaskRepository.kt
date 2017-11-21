package taskly.system.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import taskly.system.domain.Task

/**
 * @author Alexandru Stoica
 * @version 1.0
 */

@Repository
interface TaskRepository : CrudRepository<Task, Int> {
    fun findTaskById(id: Int): Task?
    fun findTasksByLocation(location: String): List<Task>
}
