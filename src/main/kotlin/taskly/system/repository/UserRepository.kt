package taskly.system.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import taskly.system.domain.UserEntity

/**
 * @author Alexandru Stoica
 * @version 1.0
 */

@Repository
interface UserRepository: CrudRepository<UserEntity, Int> {
    fun findUserById(id: Int): UserEntity?
    fun findByUsername(name: String): UserEntity?
}