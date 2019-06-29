package com.mapexample.vehicles

interface VehicleContract {
    interface View{
        fun onVehicleListLoaded(vehicle:MutableList<Vehicle>)
    }

    interface Presenter{
        fun getVehicles(lat:Float,lng:Float)
    }
}