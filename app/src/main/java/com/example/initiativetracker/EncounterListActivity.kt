package com.example.initiativetracker

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class EncounterListActivity : AppCompatActivity() {

    private lateinit var db: Database
    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_encounter_list)

        db = Database(this)

        userId = intent.getIntExtra("userId", -1)

        val encounterNameInput = findViewById<EditText>(R.id.encounterNameInput)
        val encounterDescriptionInput = findViewById<EditText>(R.id.encounterDescriptionInput)
        val addEncounterButton = findViewById<Button>(R.id.addEncounterButton)
        val encounterList = findViewById<ListView>(R.id.encounterList)

        fun loadEncounters() {
            val encounters = db.getEncounters(userId)

            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, encounters)

            encounterList.adapter = adapter
        }

        addEncounterButton.setOnClickListener {

            val name = encounterNameInput.text.toString().trim()
            val description = encounterDescriptionInput.text.toString().trim()

            if (name.isBlank()) {
                Toast.makeText(this, "Enter encounter name.", Toast.LENGTH_SHORT).show()
            } else {

                val success = db.addEncounter(
                    userId,
                    name,
                    description
                )

                if (success) {

                    val newEncounterId = db.getLastEncounterId(userId)
                    val intent = Intent(this, CharacterListActivity::class.java)
                    intent.putExtra("encounterId", newEncounterId)
                    intent.putExtra("encounterName", name)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Encounter could not be created.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        encounterList.setOnItemClickListener { _, _, position, _ ->

            val selected = encounterList.getItemAtPosition(position).toString()

            val intent = Intent(this, CharacterListActivity::class.java)
            intent.putExtra("encounterName", selected)
            startActivity(intent)
        }

        loadEncounters()
    }
}