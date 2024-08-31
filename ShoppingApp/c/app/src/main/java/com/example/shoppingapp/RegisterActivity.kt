package com.example.shoppingapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if (languageSelected=="greek"){
            setContentView(R.layout.activity_register_gr)
        }
        else if (languageSelected=="english"){
            setContentView(R.layout.activity_register)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()

        val emailField: EditText = findViewById(R.id.registerTextEmailAddress)
        val passwordField: EditText = findViewById(R.id.registerTextPassword)
        val registerButton: Button = findViewById(R.id.userregisterBtn)
        val backButton: Button = findViewById(R.id.backButton)
        val registrationTitle: TextView = findViewById(R.id.registrationTitle)
        val langButton : ImageButton = findViewById(R.id.registerLanguageBtn)

        registerButton.setOnClickListener {
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email and Password cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, StartingPageActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "Registration Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        backButton.setOnClickListener {
            finish()
        }

        langButton.setOnClickListener {
            if (languageSelected=="greek"){
                languageSelected="english"
            }else if(languageSelected==="english"){
                languageSelected="greek"
            }
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()

        }
    }
}
