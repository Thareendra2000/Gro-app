package com.example.groapp.Adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.groapp.Activities.Order.RateItemActivity
import com.example.groapp.Models.CartModel
import com.example.groapp.Models.GardenModel
import com.example.groapp.Models.OrderModel
import com.example.groapp.Models.ProductModel
import com.example.groapp.R
import com.example.groapp.Services.UserSingleton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class CartCompletedAdapter(private val completedList: ArrayList<OrderModel>) :
    RecyclerView.Adapter<CartCompletedAdapter.ViewHolder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: onItemClickListener){
        mListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.activity_cart_completed_items, parent, false)
        return ViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = completedList[position]

        var productId = data.productId
        var gardenId = data.gardenId
        var cartId = data.cartId
        holder.btnRate.setOnClickListener {
            var  intent = Intent(holder.itemView.context, RateItemActivity::class.java)
            intent.putExtra("productId", productId)
            intent.putExtra("cartId", cartId)
            intent.putExtra("gardenId", gardenId)
            intent.putExtra("userId", UserSingleton.uid)
            holder.itemView.context.startActivity(intent)
        }

        cartId?.let { FirebaseDatabase.getInstance().getReference("cart").child(it) }
            ?.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val cart = dataSnapshot.getValue(CartModel::class.java)
                        if (cart != null) {
                            holder.tvOrderPrice.text = cart.totalPrice
                        }
                    } else {
                        println("data not exists")
                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle the error case here
                }
            })

        productId?.let { FirebaseDatabase.getInstance().getReference("products").child(it) }
            ?.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        println("data exists")
                        val product = dataSnapshot.getValue(ProductModel::class.java)
                        if (product != null) {
                            println("product not null")
                            holder.tvItemName.text = product.name
                            Glide.with(holder.itemView.context)
                                .load(product.img_url)
                                .into(holder.tvPendingItemImage)
                        }
                    } else {
                        println("data not exists")
                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle the error case here
                }
            })

        gardenId?.let { FirebaseDatabase.getInstance().getReference("Garden").child(it) }
            ?.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Handle the retrieved garden data here
                    if (dataSnapshot.exists()) {
                        val garden = dataSnapshot.getValue(GardenModel::class.java)
                        // Do something with the retrieved garden data
                        val gardenName = garden?.name
                        val gardenLocation = garden?.address
                        val gardenContact = garden?.phoneNo

                        holder.tvGardenName.text = gardenName
                        holder.tvGardenAddress.text = gardenLocation
                        holder.tvGardenContact.text = gardenContact
                    } else {
                        // Handle the case where the garden data does not exist
                        println("Error")
                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle the error case here
                }
            })
    }

    override fun getItemCount(): Int {
        return completedList.size
    }

    class ViewHolder(itemView: View, clickListener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {

        val tvItemName : TextView = itemView.findViewById(R.id.tvItemName)
        val tvGardenName : TextView = itemView.findViewById(R.id.tvGardenName)
        val tvOrderPrice : TextView = itemView.findViewById(R.id.tvOrderPrice)
        val tvGardenAddress : TextView = itemView.findViewById(R.id.tvGardenAddress)
        val tvGardenContact : TextView = itemView.findViewById(R.id.tvGardenContact)
        val tvPendingItemImage : ImageView = itemView.findViewById(R.id.tvPendingItemImage)
        val btnRate : Button = itemView.findViewById(R.id.btnRate)

        init {
            itemView.setOnClickListener {
                clickListener.onItemClick(adapterPosition)
            }
        }
    }
}
