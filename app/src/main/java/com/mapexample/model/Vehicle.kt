package com.mapexample.model

class Vehicle(
    val id: Int?,
    val coordinate: Coordinate?,
    val fleetType: String?,
    val heading: Double?
    ) {

    data class Builder(
        var id: Int? = null,
        var coordinate: Coordinate? = null,
        var fleetType: String? = null,
        var heading: Double? = null
    ) {

        fun id(id: Int) = apply { this.id = id }
        fun coordinate(condiments: Coordinate) = apply { this.coordinate = condiments }
        fun fleetType(fleetType: String) = apply { this.fleetType = fleetType }
        fun heading(heading: Double) = apply { this.heading = heading }
        fun build() = Vehicle(id, coordinate, fleetType, heading)
    }
}
