package com.mapexample.view.vehicles

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.mapexample.network.dto.VehicleResponseDto
import com.mapexample.network.service.VehicleService
import com.mapexample.rx.TrampolineSchedulerProvider
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.HttpException


@RunWith(MockitoJUnitRunner::class)

class VehiclePresenterTest {

    private var view: VehicleContract.View = mock()
    private val service: VehicleService = mock()

    private var schedulerProvider = TrampolineSchedulerProvider()
    private lateinit var presenter: VehiclePresenter
    private lateinit var testScheduler: TestScheduler

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        testScheduler = TestScheduler()
        val disposable = CompositeDisposable()
        presenter = VehiclePresenter(view, service, schedulerProvider, disposable)
    }

    @Test
    fun test_getVehicle_should_return_nothing_when_null_bounder() {
        presenter.getVehicles(null)
        verify(view, never()).onVehicleListLoaded(any())
    }

    @Test
    fun test_getVehicle_should_return_list_of_vehicle() {
        val responseDto = mock<VehicleResponseDto>()
        doReturn(Observable.just(responseDto))
            .`when`(service)
            .loadVehicles(any(), any(), any(), any())
        presenter.getVehicles(LatLngBounds(LatLng(any(), any()), LatLng(any(), any())))
        verify(view).hideLoading()
        verify(view).onVehicleListLoaded(listOf())
    }

    @Test
    fun test_getVehicle_should_return_error_msg_on_service_error_message() {
        val dummyErrorMessage = "Dummy Error Msg"
        doReturn(Observable.error<Exception>(Exception(dummyErrorMessage))).`when`(service)
            .loadVehicles(any(), any(), any(), any())

        presenter.getVehicles(LatLngBounds(LatLng(any(), any()), LatLng(any(), any())))
        verify(view).hideLoading()
        verify(view).onError(dummyErrorMessage)
    }

    @Test
    fun test_getVehicle_should_return_http_exception() {
        val httpExceptionMock:HttpException = mock()
        doReturn(Observable.error<HttpException>(httpExceptionMock)).`when`(service)
            .loadVehicles(any(), any(), any(), any())

        presenter.getVehicles(LatLngBounds(LatLng(any(), any()), LatLng(any(), any())))
        verify(view).hideLoading()
        verify(view).onNetworkError()
    }
}