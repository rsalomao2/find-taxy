package com.mapexample.view.vehicles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.model.LatLngBounds
import com.mapexample.model.builder.LatLngBoundsBuilder
import com.mapexample.R
import com.mapexample.model.Bounds
import com.mapexample.model.Vehicle
import com.mapexample.util.toast
import com.mapexample.view.MainActivity
import com.mapexample.view.maps.MapsFragment
import kotlinx.android.synthetic.main.fragment_car_list.*

class VehicleListFragment : Fragment(), VehicleContract.View {

    private val hamburg = Bounds(53.394655, 10.099891, 53.694865, 9.757589)

    private lateinit var mAdapter: VehicleListAdapter

    override fun onVehicleListLoaded(vehicleList: List<Vehicle>) {
        mAdapter.updateList(vehicleList)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_car_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mPresenter = VehiclePresenter(this, context)
        setRecycleView()
        setToolBar()
        mPresenter.getVehicles(getLatLngBound(hamburg))
    }

    private fun getLatLngBound(hamburg: Bounds): LatLngBounds {
        return LatLngBoundsBuilder.Builder()
            .northeast(hamburg.lat1, hamburg.lng1)
            .southwest(hamburg.lat2, hamburg.lng2)
            .build()
    }

    private fun setToolBar() {
        val act = requireActivity() as MainActivity
        act.supportActionBar?.title = getString(R.string.l_fragment_list_vehicle_type_title)
        act.supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    private fun setRecycleView() {
        mAdapter = VehicleListAdapter(mutableListOf(), object :
            VehicleListAdapter.OnItemClickListener {
            override fun onItemClick(view: View, item: Vehicle) {
                fragmentManager?.beginTransaction()
                    ?.add(R.id.frag_container, MapsFragment.newInstance(item))
                    ?.addToBackStack("")
                    ?.commit()
            }
        })
        rviList.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        rviList.adapter = mAdapter
    }

    override fun onError(message: String?) {
        if (!message.isNullOrEmpty())
            toast(message)
    }

    override fun showLoading() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        progressBar.visibility = View.GONE
    }
}
