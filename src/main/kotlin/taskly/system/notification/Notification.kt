package taskly.system.notification

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
import taskly.system.user.User

@Entity
@ApiModel
@Table(name = "Notification")
data class Notification(

        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id_notification")
        val id: Int,
        val text: String,

        @ApiModelProperty(hidden = true)
        @CreationTimestamp @Temporal(TemporalType.TIMESTAMP)
        val date: Calendar,

        @JsonIgnore
        @ApiModelProperty(hidden = true)
        @ManyToOne(fetch = FetchType.LAZY,
                targetEntity = User::class)
        @JoinColumn(name = "id_user")
        val user: User? = null) : Serializable {

        constructor(user: User): this(0, "default", Calendar.getInstance(), user)
        constructor() : this(0, "default", Calendar.getInstance())
}