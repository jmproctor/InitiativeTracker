package com.example.initiativetracker

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.TextView

class CharacterAdapter(
    context: Context,
    private val characters: ArrayList<CharacterData>
) : ArrayAdapter<CharacterData>(context, 0, characters) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val row = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.character_row, parent, false)

        val character = characters[position]

        val nameText = row.findViewById<TextView>(R.id.rowNameText)
        val initiativeText = row.findViewById<TextView>(R.id.rowInitiativeText)
        val healthBar = row.findViewById<ProgressBar>(R.id.rowHealthBar)
        val healthText = row.findViewById<TextView>(R.id.rowHealthText)

        val percent =
            if (character.maxHealth > 0) {
                character.currentHealth * 100 / character.maxHealth
            } else {
                0
            }

        nameText.text = character.name
        initiativeText.text = "Initiative: ${character.initiative}"
        healthText.text = "${character.currentHealth}/${character.maxHealth} HP ($percent%)"

        healthBar.progress = percent

        when {
            percent >= 76 -> healthBar.progressDrawable.setTint(Color.GREEN)
            percent >= 36 -> healthBar.progressDrawable.setTint(Color.YELLOW)
            percent >= 1 -> healthBar.progressDrawable.setTint(Color.RED)
            else -> healthBar.progressDrawable.setTint(Color.GRAY)
        }

        return row
    }
}