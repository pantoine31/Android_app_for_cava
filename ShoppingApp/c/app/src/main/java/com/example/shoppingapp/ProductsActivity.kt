package com.example.shoppingapp

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class ProductsActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (languageSelected=="greek"){
            setContentView(R.layout.activity_products_gr)
        }
        else if (languageSelected=="english"){
            setContentView(R.layout.activity_products)
        }

        db = FirebaseFirestore.getInstance()

        val backButton: Button = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ProductAdapter(getProducts(), this)
    }

    private fun getProducts(): List<Product> {
        return listOf(
            Product("Drink 1", "Unleash the fiery essence of ancient dragons with every sip of this potent elixir. Infused with rare phoenix feathers and distilled under a full moon, Dragon's Breath Elixir promises a burst of spicy, smoky flavors that will set your taste buds ablaze. Perfect for daring adventurers and those who crave a bit of magic in their lives.", 10.0, R.drawable.drink1),
            Product("Drink 2", "Travel to the farthest reaches of the galaxy with Galactic Stardust Sparkler. This luminescent, fizzy drink is crafted from the sparkling waters of a distant nebula and sprinkled with real stardust. Each bottle shimmers with an otherworldly glow, and the taste is out of this world, blending cosmic berries and a hint of extraterrestrial citrus.", 12.0, R.drawable.drink2),
            Product("Drink 3", "Dive deep into the ocean's enchanting embrace with Mermaid's Melody Tonic. This mesmerizing blue potion captures the essence of the sea, with hints of ocean mist, tropical fruits, and a whisper of sea breeze. Enriched with the harmonious songs of mermaids, it's a drink that soothes the soul and tantalizes the senses.", 8.0, R.drawable.drink3),
            Product("Drink 4", "Conjure up some magic with Witch's Brew Potion, a dark and mysterious concoction bubbling with enchantment. Brewed in ancient cauldrons and stirred with a silver wand, this potion blends forbidden forest berries with a dash of enchanted herbs. Perfect for casting spells or simply enjoying a night of mystical delight.", 15.0, R.drawable.drink4),
            Product("Drink 5", "Set sail on a swashbuckling adventure with Pirate's Plunder Grog. This robust and hearty drink is aged in oak barrels buried on secret islands and infused with the spirit of the high seas. With notes of rich caramel, spiced rum, and a hint of tropical plunder, it’s the perfect companion for any pirate's treasure hunt.", 9.0, R.drawable.drink5)
        )
    }
}
