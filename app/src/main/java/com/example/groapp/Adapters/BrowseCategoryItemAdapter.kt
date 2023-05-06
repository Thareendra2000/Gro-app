package com.example.groapp.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.groapp.R
import com.example.groapp.Models.ProductModel

class BrowseCategoryItemAdapter(private val productList: ArrayList<ProductModel>) :
    RecyclerView.Adapter<BrowseCategoryItemAdapter.ViewHolder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: onItemClickListener){
        mListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.activity_market_browse_category_items_item, parent, false)
        return ViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentData = productList[position]
        holder.tvItemName.text = currentData.name
        holder.tvItemDescription.text = currentData.description
        holder.tvItemPrice.text =  currentData.unit_price.toString()+ " per " + currentData.unit
        holder.tvItemBestBefore.text = currentData.best_before.toString()
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    class ViewHolder(itemView: View, clickListener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {
        val tvItemName : TextView = itemView.findViewById(R.id.tvItemName)
        val tvItemDescription : TextView = itemView.findViewById(R.id.tvItemDescription)
        val tvItemPrice : TextView = itemView.findViewById(R.id.tvItemPrice)
        val tvItemBestBefore : TextView = itemView.findViewById(R.id.tvItemBestBefore)

        init {
            itemView.setOnClickListener {
                clickListener.onItemClick(adapterPosition)
            }
        }
    }
}
