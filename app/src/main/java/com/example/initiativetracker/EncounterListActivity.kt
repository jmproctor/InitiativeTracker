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
    private lateinit var encounterList: ListView
    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_encounter_list)

        db = Database(this)

        userId = intent.getIntExtra("userId", -1)

        val encounterNameInput = findViewById<EditText>(R.id.encounterNameInput)
        val encounterDescriptionInput = findViewById<EditText>(R.id.encounterDescriptionInput)
        val addEncounterButton = findViewById<Button>(R.id.addEncounterButton)
        encounterList = findViewById(R.id.encounterList)



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
                    encounterNameInput.text.clear()
                    encounterDescriptionInput.text.clear()

                } else {
                    Toast.makeText(this, "Encounter could not be created.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        encounterList.setOnItemClickListener { _, _, position, _ ->

            val selected = encounterList.getItemAtPosition(position).toString()
            val encounterId = selected.substringBefore(" - ").toInt()
            val encounterName = selected.substringAfter(" - ")
            val intent = Intent(this, CharacterListActivity::class.java)

            intent.putExtra("encounterId", encounterId)
            intent.putExtra("encounterName", encounterName)
            startActivity(intent)
        }

        loadEncounters()
    }

    fun loadEncounters() {
        val encounters = db.getEncounters(userId)

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, encounters)

        encounterList.adapter = adapter
    }
    override fun onResume() {
        super.onResume()
        loadEncounters()
    }
}