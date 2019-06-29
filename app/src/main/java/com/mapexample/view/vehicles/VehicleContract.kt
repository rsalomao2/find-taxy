package com.mapexample.view.vehicles

import com.mapexample.model.Vehicle

interface VehicleContract {
    interface View{
        fun onVehicleListLoaded(vehicle: List<Vehicle>)
        fun onError(message: String?)
        fun showLoading()
        fun hideLoading()

    }

    interface Presenter{
        fun getVehicles(lat1: Float, lng1: Float,lat2: Float, lng2: Float)
        fun onStop()
    }
}