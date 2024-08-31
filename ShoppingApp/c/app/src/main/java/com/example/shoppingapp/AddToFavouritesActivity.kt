package com.example.shoppingapp

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AddToFavouritesActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var favRecyclerView: RecyclerView
    private lateinit var usernameFavTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (languageSelected=="greek"){
            setContentView(R.layout.activity_add_to_favourites_gr)
        }
        else if (languageSelected=="english"){
            setContentView(R.layout.activity_add_to_favourites)
        }


        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        val backFavButton: Button = findViewById(R.id.backFavButton)
        backFavButton.setOnClickListener {
            finish()
        }

        usernameFavTextView = findViewById(R.id.usernameFavTextView)
        val user = auth.currentUser

        val displayName = user?.displayName ?: user?.email ?: "User"
        usernameFavTextView.text = "Your Favourites are here, $displayName"
        if (languageSelected=="greek"){
            usernameFavTextView.text = "Τα αγαπημένα σας, $displayName"
        }

        favRecyclerView = findViewById(R.id.favRecyclerView)
        favRecyclerView.layoutManager = LinearLayoutManager(this)

        loadFavouriteItems()
    }

    private fun loadFavouriteItems() {
        val user = auth.currentUser
        user?.let {
            db.collection("fav")
                .whereEqualTo("userId", it.uid)
                .get()
                .addOnSuccessListener { documents ->
                    val favItems = documents.map { document ->
                        FavItem(
                            document.id,
                            document.getString("name") ?: "",
                            document.getString("description") ?: "",
                            document.getDouble("price") ?: 0.0,
                            document.getLong("image")?.toInt() ?: 0
                        )
                    }
                    updateRecyclerView(favItems)
                }
                .addOnFailureListener {
                    // Handle the error
                }
        }
    }

    private fun updateRecyclerView(favItems: List<FavItem>) {
        favRecyclerView.adapter = FavouritesAdapter(favItems) { favItem ->
            removeFavouriteItem(favItem)
        }
    }

    private fun removeFavouriteItem(favItem: FavItem) {
        db.collection("fav").document(favItem.id)
            .delete()
            .addOnSuccessListener {
                loadFavouriteItems()
            }
            .addOnFailureListener {
                // Handle the error
            }
    }
}

data class FavItem(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val image: Int
)
