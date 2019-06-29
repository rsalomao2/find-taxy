package com.mapexample.view.vehicles

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.mapexample.R
import com.mapexample.model.Vehicle
import com.mapexample.util.toast
import kotlinx.android.synthetic.main.fragment_car_list.*

class VehicleListFragment : Fragment(), VehicleContract.View {


    private lateinit var mAdapter: VehicleListAdapter

    override fun onVehicleListLoaded(vehicle: List<Vehicle>) {
        mAdapter.updateList(vehicle)
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
        mPresenter.getVehicles(
            53.694865f,
            9.757589f,
            53.394655f,
            10.099891f
        )
    }

    private fun setRecycleView() {
        mAdapter = VehicleListAdapter(mutableListOf(), object :
            VehicleListAdapter.OnItemClickListener {
            override fun onItemClick(view: View, item: Vehicle) {
                view.findNavController().navigate(R.id.action_vehicleListFragment_to_mapsFragment)
            }
        })
        rviList.layoutManager = LinearLayoutManager(context)
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
