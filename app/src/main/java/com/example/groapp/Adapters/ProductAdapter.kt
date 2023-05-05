package com.example.groapp.Adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.groapp.Activities.Product.EditDeleteItemActivity
import com.example.groapp.Models.ProductModel
import com.example.groapp.R
import com.google.firebase.database.FirebaseDatabase


class ProductAdapter(private val productsList: List<ProductModel>) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    private lateinit var mListener: onItemClickListener

    class ViewHolder(itemView: View, clickListener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {

        val tvCatName : TextView = itemView.findViewById(R.id.tvCatName)
        val tvCatDescription : TextView = itemView.findViewById(R.id.tvCatDescription)
        val tvCatImage : ImageView = itemView.findViewById(R.id.tvCatImage)
        val tvBestBefore : TextView = itemView.findViewById(R.id.tvBestBefore)
        val editBtn : Button = itemView.findViewById(R.id.editBtn)
        val deleteBtn : Button = itemView.findViewById(R.id.deleteBtn)

        init {
            itemView.setOnClickListener {
                clickListener.onItemClick(adapterPosition)
            }
        }

    }

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: onItemClickListener){
        mListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.product_owner_pov, parent, false)
        return ViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentProduct = productsList[position]
        holder.tvCatName.text = currentProduct.name
        holder.tvCatDescription.text = currentProduct.description
        holder.tvBestBefore.text = currentProduct.best_before!!.date.toString()
        holder.editBtn.setTag(currentProduct.production_id)
        holder.deleteBtn.setTag(currentProduct.production_id)

        Glide.with(holder.itemView.context)
            .load(currentProduct.img_url)
            .into(holder.tvCatImage)

        holder.editBtn.setOnClickListener {
            val intent = Intent(holder.itemView.context, EditDeleteItemActivity::class.java)
            intent.putExtra("productID", currentProduct.production_id)
            holder.itemView.context.startActivity(intent)
        }

        holder.deleteBtn.setOnClickListener {
            val builder = AlertDialog.Builder(holder.itemView.context)
            builder.setMessage("Are you sure you want to delete this record?")
            builder.setPositiveButton("Yes") { _, _ ->
                val dbRef = FirebaseDatabase.getInstance().getReference("products").child(currentProduct.production_id.toString())
                val mTask = dbRef.removeValue()

                mTask.addOnSuccessListener {
                    Toast.makeText(holder.itemView.context, "Volunteering Deleted", Toast.LENGTH_LONG).show()
                }.addOnFailureListener { error ->
                    Toast.makeText(holder.itemView.context, "Deleting Err ${error.message}", Toast.LENGTH_LONG).show()
                }
            }
            builder.setNegativeButton("No") { _, _ -> }
            builder.show()
        }

    }

    override fun getItemCount(): Int {
        return productsList.size
    }
}
