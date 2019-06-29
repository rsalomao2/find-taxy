package com.mapexample.network.dto

import com.mapexample.model.Coordinate

class VehicleDto(
    val id: Int?,
    val coordinate: Coordinate?,
    val fleetType: String?,
    val heading: Double?
)
