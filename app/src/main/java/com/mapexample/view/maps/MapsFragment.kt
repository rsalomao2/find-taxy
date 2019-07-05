package com.mapexample.view.maps

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.mapexample.R
import com.mapexample.model.Coordinate
import com.mapexample.model.Vehicle
import com.mapexample.network.ApiClient
import com.mapexample.network.service.VehicleService
import com.mapexample.rx.BaseSchedulerProvider
import com.mapexample.rx.SchedulerProvider
import com.mapexample.util.toast
import com.mapexample.view.MainActivity
import com.mapexample.view.dialog.GeneralTextDialog
import com.mapexample.view.vehicles.VehicleContract
import com.mapexample.view.vehicles.VehicleListFragment
import com.mapexample.view.vehicles.VehiclePresenter
import io.reactivex.disposables.CompositeDisposable

class MapsFragment : Fragment(), OnMapReadyCallback, VehicleContract.View {

    private lateinit var mPresenter: VehiclePresenter
    private var mMap: GoogleMap? = null
    private var locationAllowed: Boolean = false

    private var vehicle: Vehicle? = null

    companion object {
        const val LOCATION_REQUEST_CODE = 99
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.onStop()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vehicle = arguments?.getSerializable(VehicleListFragment.ARG_VEHICLE) as Vehicle
        setMap()
        val vehicleService = ApiClient.instance.create(VehicleService::class.java)
        mPresenter = VehiclePresenter(this, vehicleService, SchedulerProvider(), CompositeDisposable())
        setToolbar()
    }

    private fun setMap() {
        val mapFrag = childFragmentManager.findFragmentById(R.id.map)
        val mapFragment = mapFrag as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun setToolbar() {
        val act = requireActivity() as MainActivity
        act.supportActionBar?.title = getString(R.string.l_fragment_map_title)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap
        mMap?.setPadding(16, 16, 16, 16)

        if (locationAllowed)
            loadCamera()
        else
            requestPermission()
    }

    private fun loadCamera() {
        if (vehicle != null) {
            showVehicleLocation()
            addMarker(vehicle)
        }
        setCameraListener()
    }

    private fun setCameraListener() {
        mMap?.setOnCameraIdleListener {
            mPresenter.getVehicles(mMap?.projection?.visibleRegion?.latLngBounds)
        }
    }

    private fun showVehicleLocation() {
        val coordinate: Coordinate? = vehicle!!.coordinate
        setCamera(coordinate?.latitude!!, coordinate.longitude)
    }

    private fun setCamera(latitude: Double, longitude: Double) {
        val latLng = LatLng(latitude, longitude)

        val cameraPosition = CameraPosition.Builder()
            .target(latLng)
            .zoom(17f)
            .bearing(10f)
            .build()
        mMap?.setOnMapLoadedCallback {
            val newCameraPosition = CameraUpdateFactory.newCameraPosition(cameraPosition)
            mMap?.animateCamera(newCameraPosition)
        }
    }

    private fun requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
            showLocationPermissionExplanation()
        else {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.isEmpty() || grantResults[0] == PackageManager.PERMISSION_DENIED) {
                requestPermission()
            } else {
                locationAllowed = true
                loadCamera()
            }
        }
    }

    private fun showLocationPermissionExplanation() {
        val dialogMsgFrag = GeneralTextDialog.newInstance(
            getString(R.string.s_location_denied),
            getString(R.string.s_location_needed_explanation)
        )
        dialogMsgFrag.setOnDialogMsgInterface(object : GeneralTextDialog.DialogMsgInterface {

            override fun onDialogMsgComplete() {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ), LOCATION_REQUEST_CODE
                )
                if (dialogMsgFrag.isVisible) {
                    dialogMsgFrag.dismiss()
                }
            }
        })
        dialogMsgFrag.show(fragmentManager, "")
    }

    private fun addMarker(vehicle: Vehicle?) {
        val locationInfo = vehicle?.coordinate
        if (locationInfo != null) {
            val latLng = LatLng(locationInfo.latitude, locationInfo.longitude)
            val markerOptions = MarkerOptions()
            val mMarker = mMap?.addMarker(markerOptions.position(latLng))

            when (vehicle.fleetType) {
                Vehicle.Type.TAXI.type -> mMarker?.setIcon(
                    bitmapDescriptorFromVector(
                        requireContext(),
                        R.drawable.ic_taxi
                    )
                )
                Vehicle.Type.POOLING.type -> mMarker?.setIcon(
                    bitmapDescriptorFromVector(
                        requireContext(),
                        R.drawable.ic_pooling
                    )
                )
            }

            mMarker?.tag = vehicle.id
            mMarker?.snippet = vehicle.fleetType
        }
    }

    /**
     * @Link https://stackoverflow.com/a/45564994
     * Code sourve to help with Vector Drawable convertion to setup the marker icon
     * I did some changes
     */
    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor {
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
        val imgWidth = 150
        val imgHeight = 150
        vectorDrawable!!.setBounds(0, 0, imgWidth, imgHeight)
        val bitmap =
            Bitmap.createBitmap(imgWidth, imgHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    override fun onVehicleListLoaded(vehicleList: List<Vehicle>) {
        vehicleList.forEach {
            addMarker(it)
        }
    }

    override fun onError(message: String) {
        if (!message.isBlank())
            toast(message)
    }

    override fun showLoading() {

    }

    override fun hideLoading() {

    }

    override fun onNetworkError() {
        getString(R.string.e_network)
    }
}
