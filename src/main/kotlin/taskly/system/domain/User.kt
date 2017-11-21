package taskly.system.domain

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.validator.constraints.Email
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.io.Serializable
import java.util.*
import javax.persistence.*

/**
 * @author Alexandru Stoica
 * @version 1.0
 */

@Entity
@Table(name = "User", uniqueConstraints =
arrayOf(UniqueConstraint(columnNames = arrayOf("username", "email"))))
data class User
constructor(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Int,
        val name: String,
        private val username: String,
        private val password: String,
        @Email val email: String,
        val profileImageUrl: String,
        @Temporal(TemporalType.TIMESTAMP) @CreationTimestamp
        val date: Calendar,

        @ManyToMany(mappedBy = "users", cascade = arrayOf(CascadeType.PERSIST))
        val tasks: Set<Task> = setOf()) : Serializable, UserDetails {

    fun addTask(task: Task): User = copy(tasks = tasks + task)

    override fun getUsername(): String = username

    override fun getPassword(): String = password

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> =
            mutableListOf(SimpleGrantedAuthority("ROLE_USER"))

    override fun isEnabled(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    constructor() : this(0, "default", "default", "default", "default@email.com", "default", Calendar.getInstance())

}
