package com.example.shoppingapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class StartingPageActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (languageSelected=="greek"){
            setContentView(R.layout.activity_starting_page_gr)
        }
        else if (languageSelected=="english"){
            setContentView(R.layout.activity_starting_page)
        }

        db = FirebaseFirestore.getInstance()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // koumpi about us
        val aboutUsBtn: Button = findViewById(R.id.aboutUsBtn)
        aboutUsBtn.setOnClickListener {
            val intent = Intent(this, AboutUsActivity::class.java)
            startActivity(intent)
        }

        //koumpi contact
        val contactBtn: Button = findViewById(R.id.contactBtn)
        contactBtn.setOnClickListener {
            val intent = Intent(this, ContactActivity::class.java)
            startActivity(intent)
        }

        // koumpi gia epeksergasia profil
        val pfpButton: Button = findViewById(R.id.pfpBtn)
        pfpButton.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        // koumpi gia emfanisi products
        val productsButton: Button = findViewById(R.id.productsBtn)
        productsButton.setOnClickListener {
            val intent = Intent(this, ProductsActivity::class.java)
            startActivity(intent)
        }

        //koumpi gia to kalathi
        val cartButton: Button = findViewById(R.id.cartBtn)
        cartButton.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }


        //koumpi gia agapimena
        val favButton: Button = findViewById(R.id.favBtn)
        favButton.setOnClickListener {
            val intent = Intent(this, AddToFavouritesActivity::class.java)
            startActivity(intent)
        }

    }

    private fun fetchProducts() {
        db.collection("Products")
            .get()
            .addOnSuccessListener { result ->
                val products = result.map { document ->
                    Product(
                        name = document.getString("name") ?: "",
                        description = document.getString("description") ?: "",
                        price = document.getDouble("price") ?: 0.0,
                        image = document.getLong("image")?.toInt() ?: 0
                    )
                }
                productAdapter = ProductAdapter(products, this)
                recyclerView.adapter = productAdapter
            }
            .addOnFailureListener { exception ->
                Log.w("MainActivity", "Error getting documents: ", exception)
            }
    }
}
