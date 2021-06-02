package com.example.alzheimerapp.controllers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.alzheimerapp.R

class MainActivity : AppCompatActivity() {
    lateinit var user: ImageView
    lateinit var task:ImageView
    lateinit var medicine:ImageView
    lateinit var list:ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        user=findViewById(R.id.user)
        task=findViewById(R.id.task)
        medicine=findViewById(R.id.medicine)
        list=findViewById(R.id.list)

        user.setOnClickListener {
            val source = Intent(applicationContext, AddPatientActivity::class.java)
            startActivity(source)

        }
        task.setOnClickListener {
            val source = Intent(applicationContext, MainActivity::class.java)
            startActivity(source)

        }
        medicine.setOnClickListener {
            val source = Intent(applicationContext, MainActivity::class.java)
            startActivity(source)

        }
        list.setOnClickListener {
            val source = Intent(applicationContext, ListPatientActivity::class.java)
            startActivity(source)

        }
    }
}