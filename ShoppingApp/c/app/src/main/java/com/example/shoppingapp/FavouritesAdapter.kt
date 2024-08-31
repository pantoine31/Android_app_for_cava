package com.example.shoppingapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class FavouritesAdapter(private val favItems: List<FavItem>, private val onRemoveClick: (FavItem) -> Unit) :
    RecyclerView.Adapter<FavouritesAdapter.FavViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.activity_item_fav, parent, false)
        if (languageSelected=="greek"){
            view = LayoutInflater.from(parent.context).inflate(R.layout.activity_item_fav_gr, parent, false)
        }
        return FavViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {
        val favItem = favItems[position]
        holder.productNameTextView.text = favItem.name
        holder.productDescriptionTextView.text = favItem.description
        holder.productPriceTextView.text = "$${favItem.price}"
        holder.productImageView.setImageResource(favItem.image)

        holder.removeButton.setOnClickListener {
            onRemoveClick(favItem)
        }
    }

    override fun getItemCount(): Int {
        return favItems.size
    }

    class FavViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImageView: ImageView = itemView.findViewById(R.id.favProductImageView)
        val productNameTextView: TextView = itemView.findViewById(R.id.favProductNameTextView)
        val productDescriptionTextView: TextView = itemView.findViewById(R.id.favProductDescriptionTextView)
        val productPriceTextView: TextView = itemView.findViewById(R.id.favProductPriceTextView)
        val removeButton: Button = itemView.findViewById(R.id.removeFavButton)
    }
}
