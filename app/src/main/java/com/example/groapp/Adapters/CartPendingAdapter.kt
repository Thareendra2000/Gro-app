package com.example.groapp.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.groapp.Models.CartModel
import com.example.groapp.Models.GardenModel
import com.example.groapp.Models.ProductModel
import com.example.groapp.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class CartPendingAdapter(private val cartList: ArrayList<CartModel>) :
    RecyclerView.Adapter<CartPendingAdapter.ViewHolder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: onItemClickListener){
        mListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.activity_cart_pending_items, parent, false)
        return ViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = cartList[position]
        val productId = data.productId
        val gardenId = data.garden_id

        productId?.let { FirebaseDatabase.getInstance().getReference("products").child(it) }
            ?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val product = dataSnapshot.getValue(ProductModel::class.java)
                    var productName = product?.name
                    holder.tvItemName.text = productName
                } else {
                    // Handle the case where the garden data does not exist
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

        Glide.with(holder.itemView.context)
            .load(data.image_url)
            .into(holder.tvPendingItemImage)
        holder.tvOrderPrice.text = data.totalPrice
    }


    override fun getItemCount(): Int {
        return cartList.size
    }

    class ViewHolder(itemView: View, clickListener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {

        val tvItemName : TextView = itemView.findViewById(R.id.tvItemName)
        val tvGardenName : TextView = itemView.findViewById(R.id.tvGardenName)
        val tvOrderPrice : TextView = itemView.findViewById(R.id.tvOrderPrice)
        val tvGardenAddress : TextView = itemView.findViewById(R.id.tvGardenAddress)
        val tvGardenContact : TextView = itemView.findViewById(R.id.tvGardenContact)
        val tvPendingItemImage : ImageView = itemView.findViewById(R.id.tvPendingItemImage)

        init {
            itemView.setOnClickListener {
                clickListener.onItemClick(adapterPosition)
            }
        }

    }


}
