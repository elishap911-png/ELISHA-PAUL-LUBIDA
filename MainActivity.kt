package com.example.eventapp

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.net.CookieHandler
import java.net.CookieManager
import java.net.CookiePolicy
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ✅ ENABLE SESSION ONCE
        val cookieManager = CookieManager()
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL)
        CookieHandler.setDefault(cookieManager)

        val email = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.password)
        val loginBtn = findViewById<Button>(R.id.loginBtn)
        val goRegister = findViewById<TextView>(R.id.goRegister)

        loginBtn.setOnClickListener {
            val emailText = email.text.toString().trim()
            val passwordText = password.text.toString().trim()

            if (emailText.isEmpty()) {
                email.error = "Email inahitajika"
                email.requestFocus()
                return@setOnClickListener
            }

            if (passwordText.isEmpty()) {
                password.error = "Password inahitajika"
                password.requestFocus()
                return@setOnClickListener
            }

            login(emailText, passwordText)
        }

        goRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun login(email: String, password: String) {
        thread {
            try {
                val url =
                    "http://10.0.2.2/Kazi/library_management_api/index.php?module=account&action=login" +
                            "&email=" + URLEncoder.encode(email, "UTF-8") +
                            "&password=" + URLEncoder.encode(password, "UTF-8")

                val conn = URL(url).openConnection() as HttpURLConnection
                conn.requestMethod = "GET"

                val response = conn.inputStream.bufferedReader().readText()

                runOnUiThread {
                    if (response.contains("success", true)) {
                        Toast.makeText(this, "Login imefanikiwa", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, DashboardActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(
                            this,
                            "Email au Password sio sahihi",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this, "Server haipatikani", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
