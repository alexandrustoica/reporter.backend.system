package taskly.system.domain

import net.minidev.json.annotate.JsonIgnore
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.validator.constraints.Email
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.io.Serializable
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * @author Alexandru Stoica
 * @version 1.0
 */

@Entity
@Table(name = "User", uniqueConstraints =
arrayOf(UniqueConstraint(columnNames = arrayOf("username", "email"))))
data class UserEntity @JvmOverloads constructor(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        @NotNull val id: Int,
        @NotNull val name: String,
        @NotNull private val username: String,
        @NotNull private val password: String,
        @NotNull @Email val email: String,
        @NotNull val profileImageUrl: String,
        @Temporal(TemporalType.TIMESTAMP) @CreationTimestamp
        @NotNull val date: Calendar,

        @ManyToMany(mappedBy = "users")
        @JsonIgnore @NotNull val tasks: Set<TaskEntity> = setOf()) : Serializable, UserDetails {

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