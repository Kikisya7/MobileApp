package com.example.lab2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MessageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        val messageTextView =
            findViewById<android.widget.TextView>(R.id.messageTextView)

        val message = intent.getStringExtra("message")

        messageTextView.text = message
    }
}