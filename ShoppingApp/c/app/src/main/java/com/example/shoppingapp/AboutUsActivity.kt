package com.example.shoppingapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button

class AboutUsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (languageSelected=="greek"){
            setContentView(R.layout.activity_about_us_gr)
        }
        else if (languageSelected=="english"){
            setContentView(R.layout.activity_about_us)
        }

        val backBtn: Button = findViewById(R.id.backBtn)
        backBtn.setOnClickListener {
            finish()
        }
    }
}

