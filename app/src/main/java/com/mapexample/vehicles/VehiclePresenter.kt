package com.mapexample.vehicles

class VehiclePresenter(private val view:VehicleContract.View):VehicleContract.Presenter {
    override fun getVehicles(lat: Float, lng: Float) {
        val dummyList = getDummyList()
        view.onVehicleListLoaded(dummyList)
    }
    private fun getDummyList(): MutableList<Vehicle> {
        val list = mutableListOf<Vehicle>()
        for (i in 0..100){
            list.add(Vehicle(i.toString()))
        }
        return list
    }
}