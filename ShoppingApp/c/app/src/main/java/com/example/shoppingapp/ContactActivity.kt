package com.example.shoppingapp



import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button

class ContactActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (languageSelected=="greek"){
            setContentView(R.layout.activity_contact_gr)
        }
        else if (languageSelected=="english"){
            setContentView(R.layout.activity_contact)
        }

        // to koumpi back sto contact , epistrefei ton xristi sto starting page
        val backBtn: Button = findViewById(R.id.backBtn)
        backBtn.setOnClickListener {
            finish()
        }
    }
}
