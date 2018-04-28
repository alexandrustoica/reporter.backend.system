package taskly.system.section

import com.fasterxml.jackson.annotation.JsonIgnore
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import taskly.system.notification.AsNotification
import taskly.system.report.Location
import taskly.system.report.Report
import java.io.Serializable
import javax.persistence.*

@Entity
@ApiModel
@Table(name = "CriticalSection")
@AsNotification(of = ["origin"])
data class CriticalSection(

        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id_critical_section")
        val id: Int = 0,
        val origin: Location = Location(),
        val radius: Double = 0.0,

        @JsonIgnore
        @OrderBy("id_report")
        @ApiModelProperty(hidden = true)
        @OneToMany(mappedBy = "section",
                targetEntity = Report::class)
        val reports: List<Report> = listOf()) : Serializable {

    constructor(origin: Location, radius: Double, reports: List<Report>) :
            this(0, origin, radius, reports)

    constructor(location: Location) : this(0, location, 0.0, listOf())
}