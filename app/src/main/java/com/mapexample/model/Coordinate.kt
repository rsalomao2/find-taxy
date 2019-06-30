package com.mapexample.model

class Coordinate(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
) {
    data class Builder(
        var latitude: Double = 0.0,
        var longitude: Double = 0.0
    ) {

        fun latitude(latitude: Double) = apply {this.latitude = latitude}
        fun longitude(longitude: Double) = apply { this.longitude = longitude }
        fun build() = Coordinate(latitude,longitude)
    }

}
