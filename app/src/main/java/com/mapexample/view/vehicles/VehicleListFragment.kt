package com.mapexample.view.vehicles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.android.gms.maps.model.LatLngBounds
import com.mapexample.model.builder.LatLngBoundsBuilder
import com.mapexample.R
import com.mapexample.model.Bounds
import com.mapexample.model.Vehicle
import com.mapexample.network.ApiClient
import com.mapexample.network.service.VehicleService
import com.mapexample.rx.SchedulerProvider
import com.mapexample.util.toast
import com.mapexample.view.MainActivity
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_car_list.*

class VehicleListFragment : Fragment(), VehicleContract.View {

    private val hamburg = Bounds(53.394655, 10.099891, 53.694865, 9.757589)

    private lateinit var mAdapter: VehicleListAdapter

    override fun onVehicleListLoaded(vehicleList: List<Vehicle>) {
        mAdapter.updateList(vehicleList)
    }

    companion object {
        const val ARG_VEHICLE = "clickedVehicle"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_car_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val vehicleService = ApiClient.instance.create(VehicleService::class.java)
        val mPresenter = VehiclePresenter(this, vehicleService, SchedulerProvider(), CompositeDisposable())
        setRecycleView()
        setToolbar()
        mPresenter.getVehicles(getLatLngBound(hamburg))
    }

    override fun onNetworkError() {
        getString(R.string.e_network)
    }

    private fun getLatLngBound(hamburg: Bounds): LatLngBounds {
        return LatLngBoundsBuilder.Builder()
            .northeast(hamburg.lat1, hamburg.lng1)
            .southwest(hamburg.lat2, hamburg.lng2)
            .build()
    }

    private fun setToolbar() {
        val act = requireActivity() as MainActivity
        act.supportActionBar?.title = getString(R.string.app_name)
    }

    private fun setRecycleView() {
        mAdapter = VehicleListAdapter(mutableListOf(), object :
            VehicleListAdapter.OnItemClickListener {
            override fun onItemClick(view: View, item: Vehicle) {
                val bundle = Bundle()
                bundle.putSerializable(ARG_VEHICLE, item)
                view.findNavController().navigate(R.id.action_vehicleListFragment_to_mapsFragment, bundle)
            }
        })
        rviList.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        rviList.adapter = mAdapter
    }

    override fun onError(message: String) {
        if (message.isNotEmpty())
            toast(message)
    }

    override fun showLoading() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        progressBar.visibility = View.GONE
    }
}
