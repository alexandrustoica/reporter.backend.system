package taskly.system.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import taskly.system.domain.TaskEntity

/**
 * @author Alexandru Stoica
 * @version 1.0
 */

@Repository
interface TaskRepository: CrudRepository<TaskEntity, Int> {
    fun findTaskById(id: Int): TaskEntity?
}
