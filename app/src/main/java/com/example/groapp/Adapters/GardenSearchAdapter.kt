//package com.example.groapp.Adapters
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Filter
//import android.widget.Filterable
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.example.groapp.Models.GardenModel
//import com.example.groapp.R
//
//class GardenSearchAdapter(private val gardenList: List<GardenModel>) :
//    RecyclerView.Adapter<GardenSearchAdapter.GardenViewHolder>() {
//
//    private var filteredList: List<GardenModel> = gardenList
//
//    inner class GardenViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val tvName: TextView = itemView.findViewById(R.id.gardenName)
//        val tvArea: TextView = itemView.findViewById(R.id.gardenArea)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GardenViewHolder {
//        val view =
//            LayoutInflater.from(parent.context).inflate(R.layout.garden_list_item, parent, false)
//        return GardenViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: GardenViewHolder, position: Int) {
//        val currentItem = filteredList[position]
//
//        holder.tvName.text = currentItem.name
//        holder.tvArea.text = currentItem.area
//    }
//
//    override fun getItemCount() = filteredList.size
//
//    fun filterList(keyword: List<GardenModel>) {
//        filteredList = if (keyword.isNotEmpty()) {
//            gardenList.filter { garden ->
//                garden.name?.contains(keyword, ignoreCase = true)==true ||
//                        garden.area?.contains(keyword, ignoreCase = true)==true
//            }
//        } else {
//            gardenList
//        }
//        notifyDataSetChanged()
//    }
//
//    interface OnItemClickListener {
//        fun onItemClick(position: Int)
//    }
//
//    private var listener: OnItemClickListener? = null
//
//    fun setOnItemClickListener(listener: OnItemClickListener) {
//        this.listener = listener
//    }
//}