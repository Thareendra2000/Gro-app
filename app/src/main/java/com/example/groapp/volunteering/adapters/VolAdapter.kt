package com.example.groapp.volunteering.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.groapp.R
import com.example.groapp.volunteering.models.VolunteeringModel


class VolAdapter(private val volList: ArrayList<VolunteeringModel>) :
    RecyclerView.Adapter<VolAdapter.ViewHolder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: onItemClickListener){
        mListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.vol_list_item, parent, false)
        return ViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentVol = volList[position]
        holder.tvVolName.text = currentVol.userName
        holder.tvVolGarden.text = currentVol.gardenName
        holder.tvVolHours.text = currentVol.hours
        holder.tvVolDate.text = currentVol.date

    }

    override fun getItemCount(): Int {
        return volList.size
    }

    class ViewHolder(itemView: View, clickListener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {

        val tvVolName : TextView = itemView.findViewById(R.id.tvVolName)
        val tvVolGarden : TextView = itemView.findViewById(R.id.tvVolGarden)
        val tvVolHours : TextView = itemView.findViewById(R.id.tvVolHours)
        val tvVolDate : TextView = itemView.findViewById(R.id.tvVolDate)

        init {
            itemView.setOnClickListener {
                clickListener.onItemClick(adapterPosition)
            }
        }

    }

}