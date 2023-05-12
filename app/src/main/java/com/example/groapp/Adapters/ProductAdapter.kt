package com.example.groapp.Adapters

import android.content.Intent
import android.util.Log
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
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ProductAdapter(private val prodList:  ArrayList<ProductModel>) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    private lateinit var mListener: onItemClickListener

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
        val currentProd= prodList[position]
        holder.tvProdName.text = currentProd.name
        holder.tvProdDescription.text = currentProd.description
        holder.tvProdBestBefore.text = currentProd.best_before!!.toLocaleString().toString()
        Glide.with(holder.itemView.context)
            .load(currentProd.img_url)
            .into(holder.tvProdImage)

        holder.editBtnProd.setOnClickListener {
            val intent = Intent(holder.itemView.context, EditDeleteItemActivity::class.java)

            Log.i("best before", currentProd.best_before.toString())
            intent.putExtra("name", currentProd.garden_name)
            intent.putExtra("gardenId", currentProd.garden_id)
            intent.putExtra("category", currentProd.category)
            intent.putExtra("productName", currentProd.name)
            intent.putExtra("productId", currentProd.production_id)
            intent.putExtra("unit", currentProd.unit)
            intent.putExtra("unitPrice", currentProd.unit_price.toString().toDouble())
            intent.putExtra("bestBefore", currentProd.best_before.toString())
            intent.putExtra("description", currentProd.description)
            intent.putExtra("quantity", currentProd.quantity.toString().toDouble())
            intent.putExtra("image_url", currentProd.img_url)
            intent.putExtra("rating", currentProd.rating.toString().toDouble())

            holder.itemView.context.startActivity(intent)
        }
        holder.deleteBtnProd.setOnClickListener {
            val builder = AlertDialog.Builder(holder.itemView.context)
            builder.setMessage("Are you sure you want to delete this record?")
            builder.setPositiveButton("Yes") { _, _ ->
                FirebaseDatabase.getInstance().reference.child("products").child(currentProd.production_id!!).removeValue()
                    .addOnSuccessListener{
                        Toast.makeText(holder.itemView.context, "Product Deleted", Toast.LENGTH_LONG).show()
                    }
                    .addOnFailureListener{ err ->
                        Toast.makeText(holder.itemView.context, "Deleting Err ${err.message}", Toast.LENGTH_LONG).show()
                    }
            }
            builder.setNegativeButton("No") { _, _ -> }
            builder.show()
        }
    }

    override fun getItemCount(): Int {
        return prodList.size
    }

    class ViewHolder(itemView: View, clickListener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {

        val tvProdName : TextView = itemView.findViewById(R.id.tvProdName)
        val tvProdDescription : TextView = itemView.findViewById((R.id.tvProdDescription))
        val tvProdBestBefore : TextView = itemView.findViewById((R.id.tvProdBestBefore))
        val tvProdImage : ImageView = itemView.findViewById(R.id.tvProdImage)
        val editBtnProd : Button = itemView.findViewById((R.id.editBtnProd))
        val deleteBtnProd : Button = itemView.findViewById((R.id.deleteBtnProd))

        init {
            itemView.setOnClickListener {
                clickListener.onItemClick(adapterPosition)
            }
        }

    }

}
