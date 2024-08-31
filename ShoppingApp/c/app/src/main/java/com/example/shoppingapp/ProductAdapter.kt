package com.example.shoppingapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.math.roundToInt

class ProductAdapter(private val products: List<Product>, private val context: Context) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {

        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        if (languageSelected=="greek"){
            view = LayoutInflater.from(parent.context).inflate(R.layout.item_product_gr, parent, false)
        }

        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.productNameTextView.text = product.name
        holder.productDescriptionTextView.text = product.description
        holder.productPriceTextView.text = "$${product.price}"
        holder.productImageView.setImageResource(product.image)

        // Fetch and display the average rating
        fetchAverageRating(product.name, holder)

        holder.addToCartButton.setOnClickListener {
            val quantityString = holder.productQuantityEditText.text.toString()
            if (quantityString.isNotEmpty()) {
                val quantity = quantityString.toInt()
                addToCart(product, quantity)
            } else {
                Toast.makeText(context, "Please enter a quantity", Toast.LENGTH_SHORT).show()
            }
        }

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

        holder.submitRatingButton.setOnClickListener {
            val rating = holder.userRatingBar.rating.toDouble()
            submitUserRating(product.name, rating, holder)
        }
    }

    override fun getItemCount(): Int {
        return products.size
    }

    private fun fetchAverageRating(productId: String, holder: ProductViewHolder) {
        db.collection("ratings")
            .whereEqualTo("name", productId)
            .get()
            .addOnSuccessListener { documents ->
                val ratings = documents.map { it.getDouble("rating") ?: 0.0 }
                if (ratings.isNotEmpty()) {
                    val averageRating = ratings.average()
                    val roundedAverage = (averageRating * 2).roundToInt() / 2.0
                    holder.averageRatingTextView.text = "Average Rating: $roundedAverage/5"
                } else {
                    holder.averageRatingTextView.text = "Average Rating: 0.0/5"
                }
            }
            .addOnFailureListener {
                // Handle the error
            }
    }

    private fun submitUserRating(productId: String, rating: Double, holder: ProductViewHolder) {
        val user = auth.currentUser

        user?.let {
            db.collection("ratings")
                .whereEqualTo("userId", it.uid)
                .whereEqualTo("name", productId)
                .get()
                .addOnSuccessListener { documents ->
                    if (documents.isEmpty) {
                        addNewRating(productId, rating, holder)
                    } else {
                        Toast.makeText(context, "You have already rated this product", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun addNewRating(productId: String, rating: Double, holder: ProductViewHolder) {
        val user = auth.currentUser
        user?.let {
            val ratingData = hashMapOf(
                "userId" to it.uid,
                "name" to productId,
                "rating" to rating
            )

            db.collection("ratings")
                .add(ratingData)
                .addOnSuccessListener {
                    Toast.makeText(context, "Rating submitted", Toast.LENGTH_SHORT).show()
                    // Refresh the average rating
                    fetchAverageRating(productId, holder)
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Failed to submit rating", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun addToCart(product: Product, quantity: Int) {
        val user = auth.currentUser
        user?.let {
            val cartItem = hashMapOf(
                "userId" to it.uid,
                "name" to product.name,
                "description" to product.description,
                "price" to product.price,
                "quantity" to quantity,
                "image" to product.image
            )

            db.collection("cart")
                .add(cartItem)
                .addOnSuccessListener {
                    Toast.makeText(context, "Product added to cart", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Failed to add product to cart", Toast.LENGTH_SHORT).show()
                }
        }
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

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImageView: ImageView = itemView.findViewById(R.id.productImageView)
        val productNameTextView: TextView = itemView.findViewById(R.id.productNameTextView)
        val productDescriptionTextView: TextView = itemView.findViewById(R.id.productDescriptionTextView)
        val productPriceTextView: TextView = itemView.findViewById(R.id.productPriceTextView)
        val productQuantityEditText: EditText = itemView.findViewById(R.id.productQuantityEditText)
        val addToCartButton: Button = itemView.findViewById(R.id.addToCartButton)
        val addToFavouritesButton: Button = itemView.findViewById(R.id.addToFavouritesButton)
        val averageRatingTextView: TextView = itemView.findViewById(R.id.averageRatingTextView)
        val userRatingBar: RatingBar = itemView.findViewById(R.id.userRatingBar)
        val submitRatingButton: Button = itemView.findViewById(R.id.submitRatingButton)
    }
}
