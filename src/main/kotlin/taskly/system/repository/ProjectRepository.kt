package taskly.system.repository

import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import taskly.system.domain.Project

/**
 * @author Alexandru Stoica
 * @version 1.0
 */

@Repository
interface ProjectRepository: CrudRepository<Project, Int> {
    fun findProjectById(id: Int): Project?
    fun findProjectsByName(name: String): List<Project>

    @Transactional
    @Modifying
    @Query("update Project p set p.name = :name where p.id = :id")
    fun updateByName(@Param("id") id: Int, @Param("name") name: String)
}