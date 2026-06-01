package com.example.initiativetracker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var db: Database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = Database(this)

        val usernameInput = findViewById<EditText>(R.id.usernameInput)
        val passwordInput = findViewById<EditText>(R.id.passwordInput)

        val registerButton = findViewById<Button>(R.id.registerButton)
        val loginButton = findViewById<Button>(R.id.loginButton)

        registerButton.setOnClickListener {

            val username = usernameInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Username and password are required.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val success = db.registerUser(username, password)

            if (success) {

                val userId = db.loginUser(username, password)

                Toast.makeText(this, "Account created successfully.", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, EncounterListActivity::class.java)
                intent.putExtra("userId", userId)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Username already exists.", Toast.LENGTH_SHORT).show()
            }
        }

        loginButton.setOnClickListener {

            val username = usernameInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Enter username and password.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val userId = db.loginUser(username, password)

            if (userId != -1) {
                val intent = Intent(this, EncounterListActivity::class.java)
                intent.putExtra("userId", userId)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Invalid username or password.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}