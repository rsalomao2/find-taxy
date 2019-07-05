package com.mapexample.network.dto

import com.google.gson.annotations.SerializedName

class VehicleResponseDto(@SerializedName("poiList") val vehicleList: List<VehicleDto>)
