package com.example.shoppingapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class ProfileActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (languageSelected=="greek"){
            setContentView(R.layout.activity_profile_gr)
        }
        else if (languageSelected=="english"){
            setContentView(R.layout.activity_profile)
        }

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        val emailTextView: TextView = findViewById(R.id.emailTextView)
        val passwordTextView: TextView = findViewById(R.id.passwordTextView)
        val logoutButton: Button = findViewById(R.id.logoutButton)
        val backButton: Button = findViewById(R.id.backButton)
        val deleteAccountButton: Button = findViewById(R.id.deleteAccountButton)

        emailTextView.text = user?.email
        // Δεν μπορούμε να τραβήξουμε τον κωδικό του χρήστη από το Firebase για λόγους ασφαλείας.
        // Μπορούμε να αφήσουμε το πεδίο κενό ή να το αντικαταστήσουμε με κάποιο placeholder.
        passwordTextView.text = "********"

        backButton.setOnClickListener {
            finish()
        }

        logoutButton.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        deleteAccountButton.setOnClickListener {
            user?.delete()
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Account Deleted Successfully", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Account Deletion Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}



