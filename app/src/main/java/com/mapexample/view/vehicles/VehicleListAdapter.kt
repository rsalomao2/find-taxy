package com.mapexample.view.vehicles

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mapexample.R
import com.mapexample.model.Vehicle
import kotlinx.android.synthetic.main.layout_car_list_item.view.*

class VehicleListAdapter(private val items: MutableList<Vehicle>, private val listener: OnItemClickListener) :
    androidx.recyclerview.widget.RecyclerView.Adapter<VehicleListAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(view: View, item: Vehicle)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_car_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(items[position], listener)
    }

    fun updateList(vehicle: List<Vehicle>) {
        items.clear()
        items.addAll(vehicle)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        fun bindView(vehicle: Vehicle, listener: OnItemClickListener) {
            when (vehicle.fleetType) {
                Vehicle.Type.POOLING.type -> {
                    itemView.ivThumbnail.setImageResource(R.drawable.ic_pooling)
                    itemView.tvType.text = Vehicle.Type.POOLING.name
                }
                Vehicle.Type.TAXI.type -> {
                    itemView.ivThumbnail.setImageResource(R.drawable.ic_taxi)
                    itemView.tvType.text = Vehicle.Type.TAXI.name
                }
            }
            itemView.setOnClickListener {
                listener.onItemClick(it, vehicle)
            }

        }
    }
}



