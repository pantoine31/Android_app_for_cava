package com.example.shoppingapp

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CartActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var cartRecyclerView: RecyclerView
    private lateinit var totalCostTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (languageSelected=="greek"){
            setContentView(R.layout.activity_cart_gr)
        }
        else if (languageSelected=="english"){
            setContentView(R.layout.activity_cart)
        }

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        val backButton: Button = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }

        val usernameTextView: TextView = findViewById(R.id.usernameTextView)
        val user = auth.currentUser

        val displayName = user?.displayName ?: user?.email ?: "User"
        if (languageSelected=="greek"){
            usernameTextView.text = "Το καλάθι σας, $displayName"
        }
        else if (languageSelected=="english"){
            usernameTextView.text = "Your Products are here, $displayName"
        }

        cartRecyclerView = findViewById(R.id.cartRecyclerView)
        cartRecyclerView.layoutManager = LinearLayoutManager(this)
        totalCostTextView = findViewById(R.id.totalCostTextView)

        loadCartItems()
    }

    private fun loadCartItems() {
        val user = auth.currentUser
        user?.let {
            db.collection("cart")
                .whereEqualTo("userId", it.uid)
                .get()
                .addOnSuccessListener { documents ->
                    val cartItems = documents.map { document ->
                        CartItem(
                            document.id,
                            document.getString("name") ?: "",
                            document.getDouble("price") ?: 0.0,
                            document.getLong("quantity")?.toInt() ?: 0,
                            document.getLong("image")?.toInt() ?: 0
                        )
                    }
                    updateRecyclerView(cartItems)
                }
                .addOnFailureListener {
                    // Handle the error
                }
        }
    }

    private fun updateRecyclerView(cartItems: List<CartItem>) {
        val totalCost = cartItems.sumByDouble { it.price * it.quantity }
        if (languageSelected=="greek"){
            totalCostTextView.text = "Συνολικό κόστος: $$totalCost"
        }
        else if (languageSelected=="english"){
            totalCostTextView.text = "Total Cost: $$totalCost"
        }

        cartRecyclerView.adapter = CartAdapter(cartItems) { cartItem ->
            removeCartItem(cartItem)
        }
    }

    private fun removeCartItem(cartItem: CartItem) {
        db.collection("cart").document(cartItem.id)
            .delete()
            .addOnSuccessListener {
                loadCartItems()
            }
            .addOnFailureListener {
                // Handle the error
            }
    }
}

data class CartItem(
    val id: String,
    val name: String,
    val price: Double,
    val quantity: Int,
    val image: Int
)
