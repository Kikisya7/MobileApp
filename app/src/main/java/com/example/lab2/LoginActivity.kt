
package com.example.lab2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var database: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        database = DatabaseHelper(this)

        val nameEditText =
            findViewById<EditText>(R.id.nameEditText)

        val loginEditText =
            findViewById<EditText>(R.id.loginEditText)

        val registerButton =
            findViewById<Button>(R.id.registerButton)

        val loginButton =
            findViewById<Button>(R.id.loginButton)

        // Реєстрація користувача
        registerButton.setOnClickListener {

            val name = nameEditText.text.toString().trim()
            val login = loginEditText.text.toString().trim()

            if (name.isEmpty() || login.isEmpty()) {

                Toast.makeText(
                    this,
                    "Заповніть усі поля",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            val success = database.registerUser(name, login)

            if (success) {

                Toast.makeText(
                    this,
                    "Реєстрація успішна!",
                    Toast.LENGTH_SHORT
                ).show()

                openMainActivity(name)

            } else {

                Toast.makeText(
                    this,
                    "Такий логін вже існує",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // Авторизація користувача
        loginButton.setOnClickListener {

            val name = nameEditText.text.toString().trim()
            val login = loginEditText.text.toString().trim()

            if (name.isEmpty() || login.isEmpty()) {

                Toast.makeText(
                    this,
                    "Заповніть усі поля",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            if (database.checkUser(name, login)) {

                Toast.makeText(
                    this,
                    "Авторизація успішна!",
                    Toast.LENGTH_SHORT
                ).show()

                openMainActivity(name)

            } else {

                Toast.makeText(
                    this,
                    "Неправильне ім'я або логін",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun openMainActivity(name: String) {

        val preferences =
            getSharedPreferences("auth", MODE_PRIVATE)

        preferences.edit()
            .putBoolean("is_logged_in", true)
            .putString("user_name", name)
            .putLong(
                "session_end",
                System.currentTimeMillis() + 5 * 60 * 1000L
            )
            .apply()

        val intent = Intent(this, MainActivity::class.java)

        startActivity(intent)

        finish()
    }
}
