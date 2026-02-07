package com.example.eventapp

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.net.URL
import java.net.URLEncoder
import kotlin.concurrent.thread

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val name = findViewById<EditText>(R.id.name)
        val email = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.password)
        val role = findViewById<Spinner>(R.id.role)
        val registerBtn = findViewById<Button>(R.id.registerBtn)

        role.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            arrayOf("member", "organizer")
        )

        registerBtn.setOnClickListener {
            if (name.text.isEmpty() || email.text.isEmpty() || password.text.isEmpty()) {
                Toast.makeText(this, "Jaza taarifa zote", Toast.LENGTH_SHORT).show()
            } else {
                register(
                    name.text.toString(),
                    email.text.toString(),
                    password.text.toString(),
                    role.selectedItem.toString()
                )
            }
        }
    }

    private fun register(name: String, email: String, password: String, role: String) {
        thread {
            try {
                val url =
                    "http://10.0.2.2/Kazi/library_management_api/index.php?module=account&action=register" +
                            "&name=" + URLEncoder.encode(name, "UTF-8") +
                            "&email=" + URLEncoder.encode(email, "UTF-8") +
                            "&password=" + URLEncoder.encode(password, "UTF-8") +
                            "&role=" + URLEncoder.encode(role, "UTF-8")

                val response = URL(url).readText()

                runOnUiThread {
                    Toast.makeText(this, response, Toast.LENGTH_LONG).show()
                    if (response.contains("success", true)) finish()
                }

            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
