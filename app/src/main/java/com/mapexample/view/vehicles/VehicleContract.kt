package com.mapexample.view.vehicles

import com.google.android.gms.maps.model.LatLngBounds
import com.mapexample.model.Vehicle

interface VehicleContract {
    interface View{
        fun onVehicleListLoaded(vehicleList: List<Vehicle>)
        fun onError(message: String?)
        fun showLoading()
        fun hideLoading()

    }

    interface Presenter{
        fun getVehicles(latLngBounds: LatLngBounds?)
        fun onStop()
    }
}