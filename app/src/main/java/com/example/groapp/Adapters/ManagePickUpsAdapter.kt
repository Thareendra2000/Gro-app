package com.example.groapp.Adapters

import android.content.Intent
import android.os.Handler
import android.transition.Visibility
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.groapp.Activities.Cart.CartPendingCheckoutSuccessActivity
import com.example.groapp.Activities.Order.ManagePickUpActivity
import com.example.groapp.Enums.OrderStatus
import com.example.groapp.Models.CartModel
import com.example.groapp.Models.GardenModel
import com.example.groapp.Models.OrderModel
import com.example.groapp.Models.ProductModel
import com.example.groapp.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.collections.ArrayList


class ManagePickUpsAdapter (private val pickUpOrdersList:  ArrayList<OrderModel>) :
    RecyclerView.Adapter<ManagePickUpsAdapter.ViewHolder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: onItemClickListener){
        mListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.pickup_item_list, parent, false)
        return ViewHolder(itemView, mListener)
    }

    override fun getItemCount(): Int {
        return pickUpOrdersList.size
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentPickUp = pickUpOrdersList[position]

        val productId = currentPickUp.productId
        val gardenId = currentPickUp.gardenId
        val cartId = currentPickUp.cartId

        FirebaseDatabase.getInstance().getReference("cart").child(cartId!!)
            .addListenerForSingleValueEvent(object : ValueEventListener {
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
                    Log.i("Error" , databaseError.message)
                }
            })

        FirebaseDatabase.getInstance().getReference("products").child(productId!!)
            .addListenerForSingleValueEvent(object : ValueEventListener {
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
                    Log.i("Error" , databaseError.message)
                }
            })

        FirebaseDatabase.getInstance().getReference("Garden").child(gardenId!!)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Handle the retrieved garden data here
                    if (dataSnapshot.exists()) {
                        val garden = dataSnapshot.getValue(GardenModel::class.java)
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
                    Log.i("Error" , databaseError.message)
                }
            })

        holder.completedBtn.setOnClickListener {
            var order = currentPickUp;
            order.status = OrderStatus.COMPLETED;
            FirebaseDatabase.getInstance().getReference("order").child(currentPickUp.id!!).setValue(order)
                .addOnSuccessListener{
                    Log.i("Success" , "Order updated to ${order.status.toString().toLowerCase()}")
                    Toast.makeText(holder.itemView.context, "Order updated to ${order.status.toString().toLowerCase()}", Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener{ err ->
                    Log.w("Error" , err.message.toString())
                    Toast.makeText(holder.itemView.context, "Order update failed", Toast.LENGTH_LONG).show()
                }
        }

        holder.putBackBtn.setOnClickListener {
            Toast.makeText(holder.itemView.context, "Order stock restocked", Toast.LENGTH_LONG).show()

        }

    }

    class ViewHolder(itemView: View, clickListener: ManagePickUpsAdapter.onItemClickListener) : RecyclerView.ViewHolder(itemView) {

        val tvItemName : TextView = itemView.findViewById(R.id.tvItemName)
        val tvGardenName : TextView = itemView.findViewById(R.id.tvGardenName)
        val tvOrderPrice : TextView = itemView.findViewById(R.id.tvOrderPrice)
        val tvGardenAddress : TextView = itemView.findViewById(R.id.tvGardenAddress)
        val tvGardenContact : TextView = itemView.findViewById(R.id.tvGardenContact)
        val tvPendingItemImage : ImageView = itemView.findViewById(R.id.tvPendingItemImage)
        val completedBtn : Button = itemView.findViewById(R.id.completedBtn)
        val putBackBtn : Button = itemView.findViewById(R.id.putBackBtn)


        init {
            itemView.setOnClickListener {
                clickListener.onItemClick(adapterPosition)
            }
        }

    }
}
