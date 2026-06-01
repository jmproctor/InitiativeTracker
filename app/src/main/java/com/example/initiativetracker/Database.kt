package com.example.initiativetracker

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class Database(context: Context) :
    SQLiteOpenHelper(context, "InitiativeTracker.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE Users (
                user_id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT UNIQUE NOT NULL,
                password TEXT NOT NULL
            )
        """)

        db.execSQL("""
            CREATE TABLE Encounters (
                encounter_id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER NOT NULL,
                encounter_name TEXT NOT NULL,
                encounter_description TEXT
            )
        """)

        db.execSQL("""
            CREATE TABLE Characters (
                character_id INTEGER PRIMARY KEY AUTOINCREMENT,
                encounter_id INTEGER NOT NULL,
                character_name TEXT NOT NULL,
                initiative INTEGER NOT NULL,
                max_health INTEGER NOT NULL,
                current_health INTEGER NOT NULL
            )
        """)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS Characters")
        db.execSQL("DROP TABLE IF EXISTS Encounters")
        db.execSQL("DROP TABLE IF EXISTS Users")
        onCreate(db)
    }

    fun registerUser(username: String, password: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("username", username)
            put("password", password)
        }

        return db.insert("Users", null, values) != -1L
    }

    fun loginUser(username: String, password: String): Int {
        val db = readableDatabase

        val cursor = db.rawQuery(
            """
            SELECT user_id
            FROM Users
            WHERE username = ? AND password = ?
            """,
            arrayOf(username, password)
        )

        var userId = -1

        if (cursor.moveToFirst()) {
            userId = cursor.getInt(0)
        }

        cursor.close()
        return userId
    }

    fun addEncounter(userId: Int, name: String, description: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("user_id", userId)
            put("encounter_name", name)
            put("encounter_description", description)
        }

        return db.insert("Encounters", null, values) != -1L
    }

    fun getEncounters(userId: Int): ArrayList<String> {
        val list = ArrayList<String>()
        val db = readableDatabase

        val cursor = db.rawQuery(
            """
            SELECT encounter_id, encounter_name
            FROM Encounters
            WHERE user_id = ?
            ORDER BY encounter_name
            """,
            arrayOf(userId.toString())
        )

        while (cursor.moveToNext()) {
            val id = cursor.getInt(0)
            val name = cursor.getString(1)

            list.add("$id - $name")
        }

        cursor.close()
        return list
    }

    fun getLastEncounterId(userId: Int): Int {
        val db = readableDatabase

        val cursor = db.rawQuery(
            """
            SELECT encounter_id
            FROM Encounters
            WHERE user_id = ?
            ORDER BY encounter_id DESC
            LIMIT 1
            """,
            arrayOf(userId.toString())
        )

        var encounterId = -1

        if (cursor.moveToFirst()) {
            encounterId = cursor.getInt(0)
        }

        cursor.close()
        return encounterId
    }

    fun addCharacter(
        encounterId: Int,
        name: String,
        initiative: Int,
        maxHealth: Int,
        currentHealth: Int
    ): Boolean {
        val db = writableDatabase

        val values = ContentValues().apply {
            put("encounter_id", encounterId)
            put("character_name", name)
            put("initiative", initiative)
            put("max_health", maxHealth)
            put("current_health", currentHealth)
        }

        return db.insert("Characters", null, values) != -1L
    }

    fun getCharacterNames(encounterId: Int): ArrayList<String> {
        val list = ArrayList<String>()
        val db = readableDatabase

        val cursor = db.rawQuery(
            """
            SELECT character_name, initiative, current_health, max_health
            FROM Characters
            WHERE encounter_id = ?
            ORDER BY initiative DESC
            """,
            arrayOf(encounterId.toString())
        )

        while (cursor.moveToNext()) {
            val name = cursor.getString(0)
            val initiative = cursor.getInt(1)
            val currentHealth = cursor.getInt(2)
            val maxHealth = cursor.getInt(3)

            list.add("$name | Initiative: $initiative | HP: $currentHealth/$maxHealth")
        }

        cursor.close()
        return list
    }

    fun getCharacterIds(encounterId: Int): ArrayList<Int> {
        val list = ArrayList<Int>()
        val db = readableDatabase

        val cursor = db.rawQuery(
            """
        SELECT character_id
        FROM Characters
        WHERE encounter_id = ?
        ORDER BY initiative DESC
        """,
            arrayOf(encounterId.toString())
        )

        while (cursor.moveToNext()) {
            list.add(cursor.getInt(0))
        }

        cursor.close()
        return list
    }
    //Pulls Character Details for the Details Page
    fun getCharacterDetails(characterId: Int): ArrayList<String> {

        val details = ArrayList<String>()
        val db = readableDatabase
        val cursor = db.rawQuery(
            """
        SELECT character_name,
               initiative,
               current_health,
               max_health
        FROM Characters
        WHERE character_id = ?
        """,
            arrayOf(characterId.toString())
        )

        if (cursor.moveToFirst()) {
            details.add(cursor.getString(0))
            details.add(cursor.getInt(1).toString())
            details.add(cursor.getInt(2).toString())
            details.add(cursor.getInt(3).toString())
        }

        cursor.close()

        return details
    }
}