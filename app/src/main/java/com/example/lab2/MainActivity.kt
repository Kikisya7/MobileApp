package com.example.lab2

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import java.util.Locale

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val weatherSpinner = findViewById<Spinner>(R.id.weatherSpinner)
        val button1 = findViewById<Button>(R.id.button1)
        val resultText = findViewById<TextView>(R.id.resultText)
        val mainLayout = findViewById<LinearLayout>(R.id.mainLayout)
        val languageSpinner = findViewById<Spinner>(R.id.languageSpinner)

        // Поле для введення повідомлення
        val messageEditText =
            findViewById<EditText>(R.id.messageEditText)

        // Кнопка для відкриття повідомлення у новому вікні
        val openMessageButton =
            findViewById<Button>(R.id.openMessageButton)

        // Кнопка для надсилання через інші додатки
        val shareMessageButton =
            findViewById<Button>(R.id.shareMessageButton)

        // Елементи таймера
        val timerTextView =
            findViewById<TextView>(R.id.timerTextView)

        val startTimerButton =
            findViewById<Button>(R.id.startTimerButton)

        val stopTimerButton =
            findViewById<Button>(R.id.stopTimerButton)

        val resetTimerButton =
            findViewById<Button>(R.id.resetTimerButton)

        // ViewModel для збереження стану таймера
        val timerViewModel =
            ViewModelProvider(this)[TimerViewModel::class.java]

        // Отримуємо збережену мову
        val preferences =
            getSharedPreferences("settings", MODE_PRIVATE)

        val savedLanguage =
            preferences.getString("language", "uk")

        // Встановлюємо правильну позицію Spinner
        languageSpinner.setSelection(
            if (savedLanguage == "en") 1 else 0,
            false
        )

        // Перемикання мови
        languageSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    val selectedLanguage =
                        if (position == 0) "uk" else "en"

                    val currentLanguage =
                        resources.configuration.locales[0].language

                    if (currentLanguage != selectedLanguage) {

                        preferences.edit()
                            .putString("language", selectedLanguage)
                            .apply()

                        setLocale(selectedLanguage)
                    }
                }

                override fun onNothingSelected(
                    parent: AdapterView<*>?
                ) {
                }
            }

        // Варіант 1 — реакція за допомогою Button1
        button1.setOnClickListener {

            when (weatherSpinner.selectedItemPosition) {

                0 -> {
                    resultText.text =
                        getString(R.string.sunny_reaction)

                    mainLayout.setBackgroundResource(
                        R.drawable.sunny
                    )
                }

                1 -> {
                    resultText.text =
                        getString(R.string.rain_reaction)

                    mainLayout.setBackgroundResource(
                        R.drawable.rain
                    )
                }

                2 -> {
                    resultText.text =
                        getString(R.string.snow_reaction)

                    mainLayout.setBackgroundResource(
                        R.drawable.snow
                    )
                }

                3 -> {
                    resultText.text =
                        getString(R.string.storm_reaction)

                    mainLayout.setBackgroundResource(
                        R.drawable.storm
                    )
                }

                4 -> {
                    resultText.text =
                        getString(R.string.fog_reaction)

                    mainLayout.setBackgroundResource(
                        R.drawable.fog
                    )
                }
            }
        }

        // Варіант 2 — реакція без Button1
        weatherSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    when (position) {

                        0 -> {
                            resultText.text =
                                getString(R.string.sunny_reaction)

                            mainLayout.setBackgroundResource(
                                R.drawable.sunny
                            )
                        }

                        1 -> {
                            resultText.text =
                                getString(R.string.rain_reaction)

                            mainLayout.setBackgroundResource(
                                R.drawable.rain
                            )
                        }

                        2 -> {
                            resultText.text =
                                getString(R.string.snow_reaction)

                            mainLayout.setBackgroundResource(
                                R.drawable.snow
                            )
                        }

                        3 -> {
                            resultText.text =
                                getString(R.string.storm_reaction)

                            mainLayout.setBackgroundResource(
                                R.drawable.storm
                            )
                        }

                        4 -> {
                            resultText.text =
                                getString(R.string.fog_reaction)

                            mainLayout.setBackgroundResource(
                                R.drawable.fog
                            )
                        }
                    }
                }

                override fun onNothingSelected(
                    parent: AdapterView<*>?
                ) {
                }
            }

        // Відкриття повідомлення у новому вікні
        openMessageButton.setOnClickListener {

            val message =
                messageEditText.text.toString()

            val intent =
                Intent(this, MessageActivity::class.java)

            intent.putExtra("message", message)

            startActivity(intent)
        }

        // Надсилання повідомлення через інші додатки
        shareMessageButton.setOnClickListener {

            val message =
                messageEditText.text.toString()

            val sendIntent =
                Intent(Intent.ACTION_SEND)

            sendIntent.type = "text/plain"

            sendIntent.putExtra(
                Intent.EXTRA_TEXT,
                message
            )

            val shareIntent =
                Intent.createChooser(
                    sendIntent,
                    "Надіслати через"
                )

            startActivity(shareIntent)
        }

        // Функція оновлення часу
        fun updateTimerText(millis: Long) {

            val seconds = millis / 1000

            val minutesPart = seconds / 60
            val secondsPart = seconds % 60

            timerTextView.text =
                String.format(
                    Locale.getDefault(),
                    "%02d:%02d",
                    minutesPart,
                    secondsPart
                )
        }

        // Показуємо поточний час таймера
        updateTimerText(timerViewModel.timeLeft)

        // Відновлюємо таймер після повороту
        // або зміни розміру вікна
        timerViewModel.restore(

            onTick = { millis ->
                updateTimerText(millis)
            },

            onFinish = {
                updateTimerText(0)
            }
        )

        // Запуск таймера
        startTimerButton.setOnClickListener {

            timerViewModel.start(

                onTick = { millis ->
                    updateTimerText(millis)
                },

                onFinish = {
                    updateTimerText(0)
                }
            )
        }

        // Зупинка таймера
        stopTimerButton.setOnClickListener {

            timerViewModel.stop()
        }

        // Скидання таймера
        resetTimerButton.setOnClickListener {

            timerViewModel.reset()

            updateTimerText(
                timerViewModel.timeLeft
            )
        }
    }

    private fun setLocale(language: String) {

        val locale = Locale(language)

        Locale.setDefault(locale)

        val configuration =
            Configuration(resources.configuration)

        configuration.setLocale(locale)

        resources.updateConfiguration(
            configuration,
            resources.displayMetrics
        )

        recreate()
    }
}