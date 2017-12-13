package taskly.system.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import taskly.system.domain.Task
import taskly.system.domain.User

/**
 * @author Alexandru Stoica
 * @version 1.0
 */

@Repository
interface TaskRepository : PagingAndSortingRepository<Task, Int> {

    fun findTaskById(id: Int): Task?
    fun findByUsersContaining(user:User, page: Pageable): Page<Task>

    @Modifying
    @Transactional
    @Query("update Task task set task = ?2 where ?1 member task.users and task.id = ?3")
    fun updateTaskForUser(user: User, task: Task, id: Int): Int
}
