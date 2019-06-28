package com.mapexample

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.layout_car_list_item.view.*

class CarListAdapter(private val items : MutableList<Car>, private val listener: OnItemClickListener) : RecyclerView.Adapter<CarListAdapter.ViewHolder>() {

    interface OnItemClickListener{
        fun onItemClick(item: Car)
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

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(car: Car,listener: OnItemClickListener) {
            itemView.textView.text = car.text
            itemView.setOnClickListener{
                listener.onItemClick(car)
            }

        }
    }
}



