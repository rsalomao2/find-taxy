package com.mapexample


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_car_list.*

class CarListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_car_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecycleView()
    }

    private fun setRecycleView() {
        val adapter = CarListAdapter(getDummyList(), object :
            CarListAdapter.OnItemClickListener {
            override fun onItemClick(item: Car) {
                Toast.makeText(context, item.text, Toast.LENGTH_SHORT).show()
            }
        })
        rviList.layoutManager = LinearLayoutManager(context)
        rviList.adapter = adapter
    }

    private fun getDummyList(): MutableList<Car> {
        val list = mutableListOf<Car>()
        for (i in 0..100){
            list.add(Car(i.toString()))
        }
        return list
    }

}
