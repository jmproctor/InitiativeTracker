package com.example.initiativetracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class DetailsActivity : AppCompatActivity() {

    private lateinit var db: Database
    private var currentHealth = 0
    private var maxHealth = 0

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
        val healthChangeInput = findViewById<EditText>(R.id.healthChangeInput)
        val damageButton = findViewById<Button>(R.id.damageButton)
        val healButton = findViewById<Button>(R.id.healButton)

        val character = db.getCharacterDetails(characterId)

        if (character.isNotEmpty()) {

            currentHealth = character[2].toInt()
            maxHealth = character[3].toInt()



            nameText.text = character[0]
            initiativeText.text = "Initiative: ${character[1]}"
            currentHealthText.text = "Current Health: $currentHealth"
            maxHealthText.text = "Max Health: $maxHealth"
        }

        backButton.setOnClickListener {
            finish()
        }
        deleteButton.setOnClickListener {
            db.deleteCharacter(characterId)
            Toast.makeText(this, "Character deleted.", Toast.LENGTH_SHORT).show()
            finish()
        }
        damageButton.setOnClickListener {

            val amount = healthChangeInput.text.toString().toIntOrNull()

            if (amount != null) {
                currentHealth -= amount

                if (currentHealth < 0) {
                    currentHealth = 0
                }

                db.updateCharacterHealth(characterId, currentHealth)
                currentHealthText.text = "Current Health: $currentHealth"
                healthChangeInput.text.clear()
            }
        }
        healButton.setOnClickListener {

            val amount = healthChangeInput.text.toString().toIntOrNull()

            if (amount != null) {
                currentHealth += amount

                if (currentHealth > maxHealth) {
                    currentHealth = maxHealth
                }

                db.updateCharacterHealth(characterId, currentHealth)
                currentHealthText.text = "Current Health: $currentHealth"
                healthChangeInput.text.clear()
            }
        }
    }
}