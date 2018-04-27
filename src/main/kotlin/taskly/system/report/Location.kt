package taskly.system.report

import java.io.Serializable
import java.text.DecimalFormat
import javax.persistence.Column
import javax.persistence.Embeddable

import kotlin.math.sin

@Embeddable
data class Location constructor(
        @Column(name="latitude")
        val latitude: Double = 0.0,
        @Column(name="longitude")
        val longitude: Double = 0.0) :
        Serializable, Comparable<Location> {

    constructor() : this(0.0, 0.0)

    private fun Double.roundByEightDecimals() =
            DecimalFormat("#.#########").format(this).toDouble()

    override fun compareTo(other: Location): Int =
            if (latitude == other.latitude && longitude == other.longitude) 0
            else if (latitude > other.latitude && longitude > other.longitude) 1
            else -1

    private val earthRadius: Double = 6371e3

    fun distanceFrom(location: Location): Double {
        val alpha = Math.pow(sin(Math.toRadians(latitude - location.latitude) / 2), 2.0) +
                Math.cos(Math.toRadians(latitude)) *
                Math.cos(Math.toRadians(location.latitude)) *
                Math.pow(sin(Math.toRadians(location.longitude - longitude) / 2), 2.0)
        val circumference = 2 * Math.atan2(Math.sqrt(alpha), Math.sqrt(1 - alpha))
        return Math.round(earthRadius * circumference).toDouble()
    }

    fun destinationWith(distance: Double, angle: Double): Location {
        val _latitude = Math.asin(Math.sin(Math.toRadians(latitude) *
                Math.cos(distance / earthRadius) +
                Math.cos(Math.toRadians(latitude)) *
                Math.sin(distance / earthRadius) *
                Math.cos(angle))).roundByEightDecimals()
        val _longitude = longitude +
                Math.atan2(Math.sin(angle) *
                        Math.sin(distance / earthRadius) *
                        Math.cos(Math.toRadians(latitude)),
                        Math.cos(distance / earthRadius) -
                                Math.sin(Math.toRadians(latitude) * Math.sin(_latitude)))
                        .roundByEightDecimals()
        return Location(Math.toDegrees(_latitude), Math.toDegrees(_longitude))
    }
}