package com.example.initiativetracker

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

class CharacterListActivity : AppCompatActivity() {

    private lateinit var db: Database

    private var encounterId: Int = -1
    private var encounterName: String = ""
    private lateinit var characterList: ListView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_list)

        db = Database(this)

        characterList = findViewById(R.id.characterList)
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
    }
        override fun onResume() {
            super.onResume()
            loadCharacters()
        }

        private fun loadCharacters() {
            val characters = db.getCharacters(encounterId)

            val adapter = CharacterAdapter(this, characters)

            characterList.adapter = adapter

            characterList.setOnItemClickListener { _, _, position, _ ->
                val intent = Intent(this, DetailsActivity::class.java)
                intent.putExtra("characterId", characters[position].id)
                startActivity(intent)
            }


    }

}