package com.example.lab2

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "UsersDB", null, 1) {

    // Створення таблиці користувачів
    override fun onCreate(db: SQLiteDatabase) {

        db.execSQL(
            """
            CREATE TABLE users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                login TEXT UNIQUE NOT NULL
            )
            """.trimIndent()
        )
    }

    // Оновлення бази даних
    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        db.execSQL("DROP TABLE IF EXISTS users")
        onCreate(db)
    }

    // Додавання нового користувача
    fun registerUser(name: String, login: String): Boolean {

        val db = writableDatabase

        val values = ContentValues().apply {
            put("name", name)
            put("login", login)
        }

        return try {
            db.insertOrThrow("users", null, values)
            true
        } catch (e: Exception) {
            false
        }
    }

    // Перевірка користувача
    fun checkUser(name: String, login: String): Boolean {

        val db = readableDatabase

        val cursor = db.rawQuery(
            "SELECT * FROM users WHERE name = ? AND login = ?",
            arrayOf(name, login)
        )

        val result = cursor.count > 0

        cursor.close()

        return result
    }
}
