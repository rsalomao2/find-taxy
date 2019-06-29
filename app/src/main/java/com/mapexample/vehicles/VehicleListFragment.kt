package com.mapexample.vehicles


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.mapexample.R
import kotlinx.android.synthetic.main.fragment_car_list.*

class VehicleListFragment : Fragment(), VehicleContract.View {
    private lateinit var mAdapter: VehicleListAdapter

    override fun onVehicleListLoaded(vehicle: MutableList<Vehicle>) {
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
        val mPresenter = VehiclePresenter(this)
        setRecycleView()
        mPresenter.getVehicles(0f,0f)
    }

    private fun setRecycleView() {
        mAdapter = VehicleListAdapter(mutableListOf(), object :
            VehicleListAdapter.OnItemClickListener {
            override fun onItemClick(item: Vehicle) {
                Toast.makeText(context, item.text, Toast.LENGTH_SHORT).show()
            }
        })
        rviList.layoutManager = LinearLayoutManager(context)
        rviList.adapter = mAdapter
    }



}