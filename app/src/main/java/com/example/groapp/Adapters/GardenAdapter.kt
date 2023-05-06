package com.example.groapp.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.groapp.Models.GardenModel
import com.example.groapp.R

class GardenAdapter(private var gardenList: ArrayList<GardenModel>) :
    RecyclerView.Adapter<GardenAdapter.ViewHolder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: onItemClickListener){
        mListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.garden_list_item, parent, false)
        return ViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentGarden = gardenList[position]
        holder.gardenName.text = currentGarden.name
        holder.gardenArea.text = currentGarden.area
        holder.gardenAddress.text = currentGarden.address
    }

    override fun getItemCount(): Int {
        return gardenList.size
    }


    class ViewHolder(itemView: View, clickListener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {

        val gardenName : TextView = itemView.findViewById(R.id.gLName)
        val gardenArea : TextView = itemView.findViewById(R.id.gLArea)
        val gardenAddress : TextView = itemView.findViewById(R.id.gLAddress)

        init {
            itemView.setOnClickListener {
                clickListener.onItemClick(adapterPosition)
            }
        }

    }

}