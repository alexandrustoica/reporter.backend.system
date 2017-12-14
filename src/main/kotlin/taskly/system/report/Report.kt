package taskly.system.report

import com.fasterxml.jackson.annotation.JsonIgnore
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.hibernate.annotations.CreationTimestamp
import org.springframework.data.annotation.PersistenceConstructor
import taskly.system.domain.User
import java.io.Serializable
import java.util.*
import javax.persistence.*

@Entity
@ApiModel
@Table(name = "report")
data class Report @PersistenceConstructor constructor(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id_report")
        val id: Int = 0,
        val text: String = "default",
        val location: Location = Location(),

        @ApiModelProperty(hidden = true)
        @Temporal(TemporalType.TIMESTAMP)
        @CreationTimestamp
        val date: Calendar = Calendar.getInstance(),

        @JsonIgnore
        @ApiModelProperty(hidden = true)
        @ManyToOne(fetch = FetchType.LAZY,
                targetEntity = User::class,
                cascade = arrayOf(CascadeType.PERSIST))
        @JoinColumn(name = "id_user")
        val user: User? = null) : Serializable
