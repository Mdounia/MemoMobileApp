package com.example.alzheimerapp.controllers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.alzheimerapp.R
import com.google.firebase.auth.FirebaseAuth

class ForgetPasswordActivity : AppCompatActivity() {
    lateinit var email: EditText
    lateinit var send: Button
    lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)
        firebaseAuth= FirebaseAuth.getInstance()
        email=findViewById(R.id.email_fgt)
        send=findViewById(R.id.send)
        send.setOnClickListener {
            firebaseAuth.fetchSignInMethodsForEmail(email.text.toString()).addOnCompleteListener {
                if(it.result!!.signInMethods!!.isEmpty()){
                    Toast.makeText(applicationContext,"This email isn't registered ,create a new account !!!", Toast.LENGTH_LONG).show()

                }else{
                    firebaseAuth.sendPasswordResetEmail(email.text.toString()).addOnCompleteListener {
                        if(it.isSuccessful)
                            Toast.makeText(applicationContext,"An email has been sent to your email address !", Toast.LENGTH_LONG).show()
                    }
                }
            }

        }

    }
}