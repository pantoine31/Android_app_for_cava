package com.example.shoppingapp

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ProductDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        val product = intent.getParcelableExtra<Product>("product")

        val imageView: ImageView = findViewById(R.id.productImageView)
        val nameTextView: TextView = findViewById(R.id.productNameTextView)
        val descriptionTextView: TextView = findViewById(R.id.productDescriptionTextView)
        val priceTextView: TextView = findViewById(R.id.productPriceTextView)

        product?.let {
//            imageView.setImageResource(it.image)
            nameTextView.text = it.name
            descriptionTextView.text = it.description
            priceTextView.text = "$${it.price}"
        }
    }
}
