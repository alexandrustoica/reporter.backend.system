package taskly.system.user

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.validator.constraints.Email
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import taskly.system.report.Report
import java.io.Serializable
import taskly.system.notification.Notification
import java.util.*
import javax.persistence.*

@Entity
@ApiModel
@Table(name = "User", uniqueConstraints =
[(UniqueConstraint(columnNames = arrayOf("username", "email")))])
public data class User(

        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id_user")
        val id: Int,
        val name: String,
        private val username: String,
        private val password: String,
        @Email val email: String,

        @Enumerated(EnumType.STRING)
        val role: UserRole = UserRole.USER,

        @ApiModelProperty(hidden = true)
        @CreationTimestamp @Temporal(TemporalType.TIMESTAMP)
        val date: Calendar,

        @JsonIgnore
        @OrderBy("id_notification")
        @ApiModelProperty(hidden = true)
        @OneToMany(mappedBy = "user",
                targetEntity = Notification::class)
        val notifications: List<Notification> = listOf(),

        @JsonIgnore
        @OrderBy("id_report")
        @ApiModelProperty(hidden = true)
        @OneToMany(mappedBy = "user",
                targetEntity = Report::class,
                cascade = [(CascadeType.PERSIST)])
        val reports: List<Report> = listOf(),

        val expoNotificationToken: String = "") : Serializable, UserDetails {

    constructor() : this(0, "default", "default",
            "default", "default@email.com", UserRole.USER, Calendar.getInstance())

    override fun toString(): String = username

    override fun getUsername(): String = username

    override fun getPassword(): String = password

    @JsonIgnore
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> =
            mutableListOf(SimpleGrantedAuthority(
                    if (role == UserRole.POLICE) "ROLE_POLICE" else "ROLE_USER"),
                    SimpleGrantedAuthority("ROLE_USER"))

    @JsonIgnore
    override fun isEnabled(): Boolean = true

    @JsonIgnore
    override fun isCredentialsNonExpired(): Boolean = true

    @JsonIgnore
    override fun isAccountNonExpired(): Boolean = true

    @JsonIgnore
    override fun isAccountNonLocked(): Boolean = true

    fun withExpoNotificationToken(expoNotificationToken: String): User =
            copy(expoNotificationToken = expoNotificationToken)
}
