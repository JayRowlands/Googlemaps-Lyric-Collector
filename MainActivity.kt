package com.example.geolyrical

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        val passwordText = findViewById<EditText>(R.id.passwordText)
        passwordText.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val username = findViewById<EditText>(R.id.passwordText).text
                val password = findViewById<EditText>(R.id.passwordText).text

                if (username.toString() == "admin" && password.toString() == "admin") {
                    startActivity(Intent(this, MapsActivity::class.java))
                } else {
                    Toast.makeText(this, "Login credentials incorrect", Toast.LENGTH_SHORT).show()
                }
            }
            false
        })
    }
}