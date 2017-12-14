package taskly.system.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.validator.constraints.Email
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import taskly.system.report.Report
import java.io.Serializable
import java.util.*
import javax.persistence.*

/**
 * @author Alexandru Stoica
 * @version 1.0
 */

@Entity
@ApiModel
@Table(name = "User", uniqueConstraints =
arrayOf(UniqueConstraint(columnNames = arrayOf("username", "email"))))
data class User(

        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id_user")
        val id: Int,
        val name: String,
        private val username: String,
        private val password: String,
        @Email val email: String,

        @ApiModelProperty(hidden = true)
        @CreationTimestamp @Temporal(TemporalType.TIMESTAMP)
        val date: Calendar,

        @JsonIgnore
        @OrderBy("id_report")
        @ApiModelProperty(hidden = true)
        @OneToMany(mappedBy = "user",
                targetEntity = Report::class,
                cascade = arrayOf(CascadeType.PERSIST))
        val reports: List<Report> = listOf(),

        @JsonIgnore
        @OrderBy("id")
        @ApiModelProperty(hidden = true)
        @ManyToMany(mappedBy = "users", cascade = arrayOf(CascadeType.PERSIST))
        val tasks: List<Task> = listOf()) : Serializable, UserDetails {

    constructor() : this(0, "default", "default",
            "default", "default@email.com", Calendar.getInstance())

    override fun toString(): String = username

    override fun getUsername(): String = username

    override fun getPassword(): String = password

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> =
            mutableListOf(SimpleGrantedAuthority("ROLE_USER"))

    override fun isEnabled(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true
}
