package com.mapexample.vehicles

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mapexample.R
import kotlinx.android.synthetic.main.layout_car_list_item.view.*

class VehicleListAdapter(private val items : MutableList<Vehicle>, private val listener: OnItemClickListener) : RecyclerView.Adapter<VehicleListAdapter.ViewHolder>() {

    interface OnItemClickListener{
        fun onItemClick(item: Vehicle)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_car_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(items[position],listener)
    }

    fun updateList(vehicle: MutableList<Vehicle>) {
        items.clear()
        items.addAll(vehicle)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(vehicle: Vehicle, listener: OnItemClickListener) {
            itemView.textView.text = vehicle.text
            itemView.setOnClickListener{
                listener.onItemClick(vehicle)
            }

        }
    }
}



