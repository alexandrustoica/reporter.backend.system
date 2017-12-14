package taskly.system.report

import java.io.Serializable

/**
 * @author Alexandru Stoica
 * @version 1.0
 */

data class Location
constructor(val latitude: Double, val longitude: Double) : Serializable {
    constructor(): this(0.0, 0.0)
}