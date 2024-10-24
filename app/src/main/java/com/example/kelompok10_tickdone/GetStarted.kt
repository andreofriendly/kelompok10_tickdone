package com.example.kelompok10_tickdone


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.Button



class GetStarted : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_get_started) // Replace with your actual layout name

        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)

        val btnLogin: Button = findViewById(R.id.btn_login)
        val btnCreateAccount: Button = findViewById(R.id.btn_create_account)

        // Set click listener for the Login button
        btnLogin.setOnClickListener {
            val intent = Intent(this@GetStarted, Login::class.java)
            startActivity(intent)
        }

        // Set click listener for the Create Account button
        btnCreateAccount.setOnClickListener {
            val intent = Intent(this@GetStarted, Register::class.java)
            startActivity(intent)
        }
    }
}
