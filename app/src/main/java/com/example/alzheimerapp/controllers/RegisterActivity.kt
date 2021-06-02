package com.example.alzheimerapp.controllers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.alzheimerapp.R
import com.example.alzheimerapp.models.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {
    lateinit var name : EditText
    lateinit var email : EditText
    lateinit var password : EditText
    lateinit var add: Button
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var myRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        firebaseAuth= FirebaseAuth.getInstance()
        name=findViewById(R.id.name)
        email=findViewById(R.id.email)
        password=findViewById(R.id.password)
        add=findViewById(R.id.addT)
        add.setOnClickListener {
            val name = name.text.toString()
            val email = email.text.toString()
            val password = password.text.toString()
            if(TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) ){
                Toast.makeText(applicationContext, "All fields are required", Toast.LENGTH_LONG).show();
            }else{
                register(name,email,password,"TUTOR")
            }
        }
    }

    private fun register(name: String, email: String, password: String,role:String) {
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(
            OnCompleteListener {
                if(it.isSuccessful){
                    val fireBaseuser: FirebaseUser? =firebaseAuth.currentUser
                    val userId:String = fireBaseuser!!.uid
                    myRef = FirebaseDatabase.getInstance().getReference("users").child(userId)
                    var user= User(userId,name,email,password,role)
                    myRef.setValue(user).addOnCompleteListener {
                        if(it.isSuccessful){
                            val source = Intent(applicationContext, LoginActivity::class.java)
                            startActivity(source)
                            finish()
                        }else{
                            Toast.makeText(applicationContext, "error!!!", Toast.LENGTH_LONG).show()
                        }
                    }

                }else{
                    Toast.makeText(applicationContext, it.exception?.message, Toast.LENGTH_LONG).show()

                }
            })
    }
}