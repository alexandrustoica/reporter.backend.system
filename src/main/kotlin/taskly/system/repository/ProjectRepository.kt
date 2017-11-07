package taskly.system.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import taskly.system.domain.ProjectEntity

/**
 * @author Alexandru Stoica
 * @version 1.0
 */

@Repository
interface ProjectRepository: CrudRepository<ProjectEntity, Int> {
    fun findProjectById(id: Int): ProjectEntity?
}