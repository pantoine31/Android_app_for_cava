package com.example.shoppingapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ItemFavActivity(private val products: List<Product>, private val context: Context) :
    RecyclerView.Adapter<ItemFavActivity.FavViewHolder>() {

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.activity_item_fav, parent, false)
        if (languageSelected=="greek"){
            view = LayoutInflater.from(parent.context).inflate(R.layout.activity_item_fav_gr, parent, false)
        }
        return FavViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {
        val product = products[position]
        holder.productNameTextView.text = product.name
        holder.productDescriptionTextView.text = product.description
        holder.productPriceTextView.text = "$${product.price}"
        holder.productImageView.setImageResource(product.image)

        holder.addToFavouritesButton.setOnClickListener {
            val user = auth.currentUser
            user?.let {
                db.collection("fav")
                    .whereEqualTo("userId", it.uid)
                    .whereEqualTo("name", product.name)
                    .get()
                    .addOnSuccessListener { documents ->
                        if (documents.isEmpty) {
                            addToFavourites(product)
                        } else {
                            Toast.makeText(context, "Product is already in favourites", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }

    override fun getItemCount(): Int {
        return products.size
    }

    private fun addToFavourites(product: Product) {
        val user = auth.currentUser
        user?.let {
            val favItem = hashMapOf(
                "userId" to it.uid,
                "name" to product.name,
                "description" to product.description,
                "price" to product.price,
                "image" to product.image
            )

            db.collection("fav")
                .add(favItem)
                .addOnSuccessListener {
                    Toast.makeText(context, "Product added to favourites", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Failed to add product to favourites", Toast.LENGTH_SHORT).show()
                }
        }
    }

    class FavViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImageView: ImageView = itemView.findViewById(R.id.favProductImageView)
        val productNameTextView: TextView = itemView.findViewById(R.id.favProductNameTextView)
        val productDescriptionTextView: TextView = itemView.findViewById(R.id.favProductDescriptionTextView)
        val productPriceTextView: TextView = itemView.findViewById(R.id.favProductPriceTextView)
        val addToFavouritesButton: Button = itemView.findViewById(R.id.addToFavouritesButton)
    }
}
