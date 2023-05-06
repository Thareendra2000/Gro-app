package com.example.groapp
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.groapp.Activities.Cart.CartPendingCheckoutActivity
import com.example.groapp.Enums.CartStatus
import com.example.groapp.Enums.OrderStatus
import com.example.groapp.Models.CartModel
import com.example.groapp.Models.OrderModel
import com.example.groapp.Models.ProductModel
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.*

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, sdk = [28])
class CartPendingCheckoutActivityTest {

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        FirebaseApp.initializeApp(context)
    }

    @Test
    fun updateProductQuantity_quantityAddedSuccessfully() {
        // Create a mock product with a quantity of 10
        val productId = "product_id"
        val gardenId = "garden_id"
        val product = ProductModel(
            production_id = productId,
            garden_id = gardenId,
            garden_name = "Garden name",
            name = "Product",
            description = "Product description",
            unit_price = 10.0.toString(),
            unit = "kilogram",
            quantity = "10",
            best_before = SimpleDateFormat("dd/MM/yyyy").parse("12/12/2023")
        )

        // Save the product to the database
        val dbRef = FirebaseDatabase.getInstance().getReference("products").child(productId)
        dbRef.setValue(product).addOnSuccessListener {
            // Product saved successfully

            // Call the method to update the quantity
            val activity = CartPendingCheckoutActivity()
            activity.updateProductQuantity(productId, 5)

            // Wait for the method to finish executing
            Thread.sleep(1000)

            // Check that the quantity was updated correctly
            dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val updatedProduct = dataSnapshot.getValue(ProductModel::class.java)
                        assertEquals(15, updatedProduct?.quantity?.toInt())
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle the error case here
                }
            })
        }
    }

    @Test
    fun deleteTheCartItem_itemDeletedSuccessfully() {
        // Create a mock cart item
        val cartId = "cart_id"
        val cart = CartModel(
            id = cartId,
            userId = "user_id",
            productId = "product_id",
            quantity = "5",
            status = CartStatus.PENDING
        )

        // Save the cart item to the database
        val dbRef = FirebaseDatabase.getInstance().getReference("cart").child(cartId)
        dbRef.setValue(cart).addOnSuccessListener {
            // Cart item saved successfully

            // Call the method to delete the cart item
            val activity = CartPendingCheckoutActivity()
            activity.deleteTheCartItem()

            // Wait for the method to finish executing
            Thread.sleep(1000)

            // Check that the cart item was deleted correctly
            dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    assertFalse(dataSnapshot.exists())
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle the error case here
                }
            })
        }
    }

    @Test
    fun saveOrderData_orderSavedSuccessfully() {
        // Create a mock order
        val orderId = UUID.randomUUID().toString()
        val order = OrderModel(
            id = orderId,
            userId = "user_id",
            gardenId = "garden_id",
            productId = "product_id",
            cartId = "cart_id",
            note = "Order note",
            timestamp = Date(),
            status = OrderStatus.PENDING
        )

        // Call the method to save the order
        val activity = CartPendingCheckoutActivity()
        activity.saveOrderData()

        // Wait for the method to finish executing
        Thread.sleep(1000)

        // Check that the order was saved correctly
        val dbRef = FirebaseDatabase.getInstance().getReference("order").child(orderId)
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val savedOrder = dataSnapshot.getValue(OrderModel::class.java)
                    assertEquals(order.userId, savedOrder?.userId)
                    assertEquals(order.gardenId, savedOrder?.gardenId)
                    assertEquals(order.productId, savedOrder?.productId)
                    assertEquals(
                        order
                            .cartId, savedOrder?.cartId
                    )
                    assertEquals(order.note, savedOrder?.note)
                    assertEquals(order.timestamp, savedOrder?.timestamp)
                    assertEquals(order.status, savedOrder?.status)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle the error case here
            }
        })
    }
}