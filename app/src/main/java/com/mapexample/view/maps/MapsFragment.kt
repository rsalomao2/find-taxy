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
import com.mapexample.view.dialog.GeneralTextDialog


class MapsFragment : Fragment(), OnMapReadyCallback {

    private var mMap: GoogleMap? = null
    private var locationAllowed: Boolean = false

    private var vehicle: Vehicle? = null

    companion object {
        const val ARGS_VEHICLE = "vehicle"
        const val LOCATION_REQUEST_CODE = 99
        fun newInstance(it: Vehicle?): MapsFragment {
            val fragment = MapsFragment()
            val bundle = Bundle()
            bundle.putSerializable(ARGS_VEHICLE, it)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vehicle = arguments?.getSerializable(ARGS_VEHICLE) as Vehicle
        setMap()
    }

    private fun setMap() {
        val mapFrag = childFragmentManager.findFragmentById(R.id.map)
        val mapFragment = mapFrag as SupportMapFragment
        mapFragment.getMapAsync(this)
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
        if (vehicle != null){
            showVehicleLocation()
            addMarker(vehicle)
        }
    }

    private fun showVehicleLocation() {
        val coordinate: Coordinate? = vehicle!!.coordinate
        setCamera(coordinate?.latitude!!, coordinate.longitude)
    }

    private fun setCamera(latitude: Float, longitude: Float) {
        val latLng = LatLng(latitude.toDouble(), longitude.toDouble())

        val cameraPosition = CameraPosition.Builder()
            .target(latLng)
            .zoom(17f)
            .bearing(10f)
            .build()
        mMap?.setOnMapLoadedCallback {
            mMap?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        }
    }

    private fun requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION))
            showLocationPermissionExplanation()
        else{
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
            } else{
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
                    ), LOCATION_REQUEST_CODE)
            }
        })
        dialogMsgFrag.show(fragmentManager, "")
    }

    private fun addMarker(vehicle: Vehicle?) {
        val locationInfo = vehicle?.coordinate
        if (locationInfo!=null){
            val latLng = LatLng(locationInfo.latitude.toDouble(), locationInfo.longitude.toDouble())
            val markerOptions = MarkerOptions()
            val mMarker = mMap?.addMarker(markerOptions.position(latLng))

            when(vehicle.fleetType){
                Vehicle.Type.TAXI.type -> mMarker?.setIcon(bitmapDescriptorFromVector(requireContext(),R.drawable.ic_taxi))
                Vehicle.Type.POOLING.type -> mMarker?.setIcon(bitmapDescriptorFromVector(requireContext(),R.drawable.ic_pooling))
            }

            mMarker?.tag = vehicle.id
            mMarker?.snippet = vehicle.fleetType
        }
    }

    /**
     * @Link https://stackoverflow.com/a/45564994
     * Code sourve to help with Vector Drawable convertion to setup the marker icon
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
}
