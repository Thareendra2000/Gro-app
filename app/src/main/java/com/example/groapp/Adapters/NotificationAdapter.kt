package com.example.groapp.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.groapp.R
import com.example.groapp.Models.NotificationModel
import com.example.groapp.Utils.DateUtils.Companion.getTimeAgoString

class NotificationAdapter(private val notificationList: ArrayList<NotificationModel>) :
    RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: onItemClickListener){
        mListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.activity_notification_items, parent, false)
        return ViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = notificationList[position]
        holder.notificationTitle.text = data.title
        holder.timeAgo.text = data.timestamp?.let { getTimeAgoString(it) }
        holder.notificationMessage.text = data.message
    }

    override fun getItemCount(): Int {
        return notificationList.size
    }

    class ViewHolder(itemView: View, clickListener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {

        val notificationTitle : TextView = itemView.findViewById(R.id.notificationTitle)
        val timeAgo : TextView = itemView.findViewById(R.id.timeAgo)
        val notificationMessage : TextView = itemView.findViewById(R.id.notificationMessage)

        init {
            itemView.setOnClickListener {
                clickListener.onItemClick(adapterPosition)
            }
        }
    }
}
