package com.mapexample.model.builder

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds

class LatLngBoundsBuilder{

    data class Builder(
        private var northEast: LatLng? = null,
        private var southWest: LatLng? = null
    ) {

        fun northeast(lat:Double, lng:Double) = apply {
            northEast = LatLng(lat, lng)
        }

        fun southwest(lat:Double, lng:Double) = apply {
           southWest = LatLng(lat, lng)
        }


        fun build() = LatLngBounds(northEast,southWest)
    }
}