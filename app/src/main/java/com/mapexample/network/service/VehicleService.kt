package com.mapexample.network.service

import com.mapexample.network.dto.VehicleResponseDto
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query


interface VehicleService {

    @GET("/")
    fun loadVehicles(
        @Query("p1Lat") lat1: Double,
        @Query("p1Lon") lng1: Double,
        @Query("p2Lat") lat2: Double,
        @Query("p2Lon") lng2: Double
    ): Observable<VehicleResponseDto>
}