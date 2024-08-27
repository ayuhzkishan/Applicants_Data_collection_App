package com.example.jobdata

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val btnEnglish = findViewById<Button>(R.id.button_English)
        val btnHindi = findViewById<Button>(R.id.button_Hindi)
        val btnOdia = findViewById<Button>(R.id.button_Odia)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnEnglish.setOnClickListener {
            redirectToPage(EnglishActivity::class.java)
        }

        btnHindi.setOnClickListener {
            redirectToPage(HindiActivity::class.java)
        }

        btnOdia.setOnClickListener {
            redirectToPage(OdiaActivity::class.java)
        }
    }
    private fun redirectToPage(activityClass: Class<*>) {
        val intent = Intent(this, activityClass)
        startActivity(intent)
    }


}