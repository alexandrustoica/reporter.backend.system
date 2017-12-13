package taskly.system.report

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.hibernate.annotations.CreationTimestamp
import org.springframework.data.annotation.PersistenceConstructor
import java.io.Serializable
import java.util.*
import javax.persistence.*

@Entity
@ApiModel
@Table(name = "report")
data class Report @PersistenceConstructor constructor(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Int = 0,
        val text: String = "default",
        val location: Location = Location(0, 0),

        @ApiModelProperty(hidden = true)
        @Temporal(TemporalType.TIMESTAMP)
        @CreationTimestamp
        val date: Calendar = Calendar.getInstance()) : Serializable
