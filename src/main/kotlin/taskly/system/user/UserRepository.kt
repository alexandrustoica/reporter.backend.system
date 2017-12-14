package taskly.system.user

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

/**
 * @author Alexandru Stoica
 * @version 1.0
 */

@Repository
interface UserRepository : CrudRepository<User, Int> {

    fun findUserById(id: Int): User?
    fun findByUsername(name: String): User?
}
