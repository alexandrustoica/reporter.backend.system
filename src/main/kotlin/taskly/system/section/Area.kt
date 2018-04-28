package taskly.system.section

import taskly.system.report.Location
import java.io.Serializable

data class Area(val origin: Location, val radius: Double): Serializable {
    constructor(): this(Location(), 0.0)
}