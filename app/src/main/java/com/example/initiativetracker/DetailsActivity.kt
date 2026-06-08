package com.example.initiativetracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class DetailsActivity : AppCompatActivity() {

    private lateinit var db: Database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        db = Database(this)

        val characterId = intent.getIntExtra("characterId", -1)

        val nameText = findViewById<TextView>(R.id.characterNameText)
        val initiativeText = findViewById<TextView>(R.id.initiativeText)
        val currentHealthText = findViewById<TextView>(R.id.currentHealthText)
        val maxHealthText = findViewById<TextView>(R.id.maxHealthText)
        val backButton = findViewById<Button>(R.id.backButton)
        val deleteButton = findViewById<Button>(R.id.deleteCharacterButton)

        val character = db.getCharacterDetails(characterId)

        if (character.isNotEmpty()) {

            nameText.text = character[0]
            initiativeText.text = "Initiative: ${character[1]}"
            currentHealthText.text = "Current Health: ${character[2]}"
            maxHealthText.text = "Max Health: ${character[3]}"
        }

        backButton.setOnClickListener {
            finish()
        }
        deleteButton.setOnClickListener {
            db.deleteCharacter(characterId)
            Toast.makeText(this, "Character deleted.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}