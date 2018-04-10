package taskly.system.report

import java.io.Serializable

data class Location
constructor(val latitude: Double, val longitude: Double) : Serializable {
    constructor(): this(0.0, 0.0)
}