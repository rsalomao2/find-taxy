package com.mapexample.view.vehicles

import com.google.android.gms.maps.model.LatLngBounds
import com.mapexample.model.Vehicle
import com.mapexample.network.dto.VehicleResponseDto
import com.mapexample.network.service.VehicleService
import com.mapexample.rx.BaseSchedulerProvider
import io.reactivex.disposables.Disposable
import retrofit2.HttpException

class VehiclePresenter(
    private val mView: VehicleContract.View,
    private val vehicleService: VehicleService,
    private val schedulerProvider: BaseSchedulerProvider,
    private var disposable: Disposable
) :
    VehicleContract.Presenter {

    override fun getVehicles(latLngBounds: LatLngBounds?) {
        if (latLngBounds != null) {
            val northeast = latLngBounds.northeast
            val southwest = latLngBounds.southwest
            disposable = vehicleService
                .loadVehicles(northeast.latitude, northeast.longitude, southwest.latitude, southwest.longitude)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ response ->
                    mView.hideLoading()
                    mView.onVehicleListLoaded(getVehicleList(response))
                }, { error ->
                    mView.hideLoading()
                    if (error is HttpException) {
                        mView.onNetworkError()
                    } else if (error.message != null) {
                        mView.onError(error.message!!)
                    }
                })
        }
    }

    private fun getVehicleList(response: VehicleResponseDto): List<Vehicle> {
        val vehicleList = mutableListOf<Vehicle>()
        response.vehicleList.forEach { dto ->
            vehicleList.add(
                Vehicle.Builder()
                    .id(dto.id!!)
                    .coordinate(dto.coordinate!!)
                    .fleetType(dto.fleetType!!)
                    .heading(dto.heading!!)
                    .build()
            )
        }
        return vehicleList
    }

    override fun onStop() {
        disposable.dispose()
    }
}
