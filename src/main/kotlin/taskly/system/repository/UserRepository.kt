package taskly.system.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import taskly.system.domain.User

/**
 * @author Alexandru Stoica
 * @version 1.0
 */

@Repository
interface UserRepository : CrudRepository<User, Int> {

    fun findUserById(id: Int): User?
    fun findByUsername(name: String): User?
}
