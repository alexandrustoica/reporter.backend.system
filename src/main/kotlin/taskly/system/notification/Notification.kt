package taskly.system.notification

import com.fasterxml.jackson.annotation.JsonIgnore
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.hibernate.annotations.CreationTimestamp
import java.io.Serializable
import java.util.*
import javax.persistence.*
import taskly.system.user.User

@Entity
@ApiModel
@Table(name = "Notification")
data class Notification (

        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id_notification")
        val id: Int,
        val title: String,
        val message: String,

        @ApiModelProperty(hidden = true)
        @CreationTimestamp @Temporal(TemporalType.TIMESTAMP)
        val date: Calendar,
        val isRead: Boolean,

        @JsonIgnore
        @ApiModelProperty(hidden = true)
        @ManyToOne(fetch = FetchType.LAZY,
                targetEntity = User::class)
        @JoinColumn(name = "id_user")
        val user: User? = null) : Serializable {

        constructor(title: String, message: String, user: User):
                this(0, title, message, Calendar.getInstance(), false, user)
}