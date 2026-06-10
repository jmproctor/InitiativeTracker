package com.example.initiativetracker

data class CharacterData(
    val id: Int,
    val name: String,
    val initiative: Int,
    val currentHealth: Int,
    val maxHealth: Int
)