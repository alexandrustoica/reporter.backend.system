package taskly.system.report

import com.fasterxml.jackson.annotation.JsonIgnore
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.hibernate.annotations.CreationTimestamp
import org.springframework.data.annotation.PersistenceConstructor
import taskly.system.notification.AsNotification
import taskly.system.section.CriticalSection
import taskly.system.user.User
import java.io.Serializable
import java.util.*
import javax.persistence.*

@Entity
@ApiModel
@Table(name = "report")
@AsNotification(of = ["title"])
data class Report @PersistenceConstructor constructor(

        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id_report")
        val id: Int = 0,
        val text: String = "default",
        val title: String = "default",
        val location: Location = Location(),

        @Lob
        @JsonIgnore
        @Column(length = 100000)
        @ElementCollection(targetClass = Photo::class)
        val photos: List<Photo> = listOf(),

        @ApiModelProperty(hidden = true)
        @Temporal(TemporalType.TIMESTAMP)
        @CreationTimestamp
        val date: Calendar = Calendar.getInstance(),

        @JsonIgnore
        @ApiModelProperty(hidden = true)
        @ManyToOne(fetch = FetchType.LAZY,
                targetEntity = User::class,
                cascade = [(CascadeType.PERSIST)])
        @JoinColumn(name = "id_user")
        val user: User? = null,

        @JsonIgnore
        @ApiModelProperty(hidden = true)
        @ManyToOne(fetch = FetchType.LAZY,
                targetEntity = CriticalSection::class,
                cascade = [(CascadeType.PERSIST)])
        @JoinColumn(name = "id_critical_section")
        val section: CriticalSection? = null,

        val isSpam: Boolean = false,
        val isSolved: Boolean = false) : Serializable {

    constructor(location: Location) : this(0, "default", "default", location)
}
