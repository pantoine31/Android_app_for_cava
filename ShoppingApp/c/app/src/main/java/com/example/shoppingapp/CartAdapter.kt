package com.example.shoppingapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class CartAdapter(private val cartItems: List<CartItem>, private val onRemoveClick: (CartItem) -> Unit) :
    RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {

        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        if (languageSelected=="greek"){
            view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart_gr, parent, false)
        }
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartItem = cartItems[position]
        holder.productNameTextView.text = cartItem.name
        holder.productPriceTextView.text = "$${cartItem.price}"
        holder.productQuantityTextView.text = "Quantity: ${cartItem.quantity}"
        holder.productImageView.setImageResource(cartItem.image)

        holder.removeButton.setOnClickListener {
            onRemoveClick(cartItem)
        }
    }

    override fun getItemCount(): Int {
        return cartItems.size
    }

    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImageView: ImageView = itemView.findViewById(R.id.productImageView)
        val productNameTextView: TextView = itemView.findViewById(R.id.productNameTextView)
        val productPriceTextView: TextView = itemView.findViewById(R.id.productPriceTextView)
        val productQuantityTextView: TextView = itemView.findViewById(R.id.productQuantityTextView)
        val removeButton: Button = itemView.findViewById(R.id.removeButton)
    }
}
