package com.example.initiativetracker

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class CharacterListActivity : AppCompatActivity() {

    private lateinit var db: Database

    private var encounterId: Int = -1
    private var encounterName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_list)

        db = Database(this)

        encounterId = intent.getIntExtra("encounterId", -1)
        encounterName = intent.getStringExtra("encounterName") ?: ""

        val titleText = findViewById<TextView>(R.id.initiativeTitle)
        val backButton = findViewById<Button>(R.id.backToEncountersButton)

        titleText.text = encounterName

        backButton.setOnClickListener {
            finish()
        }

        val characterNameInput = findViewById<EditText>(R.id.characterNameInput)
        val initiativeInput = findViewById<EditText>(R.id.initiativeInput)
        val maxHealthInput = findViewById<EditText>(R.id.maxHealthInput)
        val currentHealthInput = findViewById<EditText>(R.id.currentHealthInput)
        val addCharacterButton = findViewById<Button>(R.id.addCharacterButton)
        val characterList = findViewById<ListView>(R.id.characterList)

        fun loadCharacters() {

            val characterListData =
                db.getCharacterNames(encounterId)

            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                characterListData
            )

            characterList.adapter = adapter
        }

        addCharacterButton.setOnClickListener {

            val name = characterNameInput.text.toString().trim()
            val initiative = initiativeInput.text.toString().toIntOrNull()
            val maxHealth = maxHealthInput.text.toString().toIntOrNull()
            val currentHealth = currentHealthInput.text.toString().toIntOrNull()


            if (
                name.isBlank() ||
                initiative == null ||
                maxHealth == null ||
                currentHealth == null
            ) {
                Toast.makeText(this, "Fill out all fields.", Toast.LENGTH_SHORT).show()
            } else {

                db.addCharacter(
                    encounterId,
                    name,
                    initiative,
                    maxHealth,
                    currentHealth
                )

                characterNameInput.text.clear()
                initiativeInput.text.clear()
                maxHealthInput.text.clear()
                currentHealthInput.text.clear()

                loadCharacters()
            }
        }

        loadCharacters()
    }
}