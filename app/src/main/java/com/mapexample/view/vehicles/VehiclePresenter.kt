package com.mapexample.view.vehicles

import android.content.Context
import com.mapexample.R
import com.mapexample.model.Vehicle
import com.mapexample.network.ApiClient
import com.mapexample.network.dto.VehicleResponseDto
import com.mapexample.network.service.VehicleService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

class VehiclePresenter(private val mView: VehicleContract.View, private val context: Context?) :
    VehicleContract.Presenter {

    private var disposable: Disposable? = null

    override fun getVehicles(lat1: Float, lng1: Float, lat2: Float, lng2: Float) {

        mView.showLoading()
        disposable = ApiClient.instance.create(VehicleService::class.java)
            .loadVehicles(lat1, lng1, lat2, lng2)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                mView.hideLoading()
                mView.onVehicleListLoaded(getVehicleList(response))
            }, { error ->
                mView.hideLoading()
                if (error is HttpException) {
                    mView.onError(context?.getString(R.string.e_network))
                } else {
                    mView.onError(error.message)
                }
            })
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
                    .build ()
            )
        }
        return vehicleList
    }

    override fun onStop() {
        disposable?.dispose()
    }
}
