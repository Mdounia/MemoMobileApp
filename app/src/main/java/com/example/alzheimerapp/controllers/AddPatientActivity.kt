package com.example.alzheimerapp.controllers

import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.alzheimerapp.R
import com.example.alzheimerapp.models.Model
import com.example.alzheimerapp.models.Patient
import com.example.alzheimerapp.models.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class AddPatientActivity : AppCompatActivity() {

    lateinit var name : EditText
    lateinit var email : EditText
    lateinit var password : EditText
    lateinit var imgP:ImageView
    lateinit var desP:EditText
    lateinit var relation:EditText
    lateinit var add: Button
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var firebaseUser: FirebaseUser
    lateinit var database: FirebaseDatabase
    lateinit var myRef: DatabaseReference
    lateinit var storageRef: StorageReference
    lateinit var imageUrl:Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_patient)
        name = findViewById(R.id.nameP)
        email = findViewById(R.id.emailP)
        password = findViewById(R.id.passwordP)
        imgP = findViewById(R.id.imgPatient)
        desP = findViewById(R.id.descriptionP)
        relation = findViewById(R.id.relationP)
        add = findViewById(R.id.addP)

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("patients")
        storageRef = FirebaseStorage.getInstance().reference
        firebaseAuth= FirebaseAuth.getInstance()
        firebaseUser= firebaseAuth.currentUser!!


        imgP.setOnClickListener {
            val galleryIntent: Intent = Intent()
            galleryIntent.setAction(Intent.ACTION_GET_CONTENT)
            galleryIntent.setType("image/*")
            startActivityForResult(galleryIntent, 2)

            add.setOnClickListener {
                val name = name.text.toString()
                val email = email.text.toString()
                val password = password.text.toString()
                val img=uploadToFirebase(imageUrl).toString()
                val des=desP.text.toString()
                val relation=relation.text.toString()


                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(applicationContext, "All fields are required", Toast.LENGTH_LONG)
                        .show();
                } else {
                   register(name, email, password, "TUTOR",img,des,relation,firebaseUser.uid)
                }
            }

        }
    }

    private fun uploadToFirebase(uri: Uri):Uri  {

        var fileRef :StorageReference=storageRef.child(System.currentTimeMillis().toString()+"."+ getFileExtension(uri))
        fileRef.putFile(uri).addOnSuccessListener {
            Toast.makeText(applicationContext, "Success", Toast.LENGTH_LONG).show()
            fileRef.downloadUrl.addOnSuccessListener {
               val  downloadUri=uri
            }
        }.addOnFailureListener {
            Toast.makeText(applicationContext, "Upload failed", Toast.LENGTH_LONG).show()
        }
        return uri
    }

    private fun getFileExtension(uri: Uri): String? {
        var cr: ContentResolver =contentResolver
        var mime: MimeTypeMap = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cr.getType(uri))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==2 && resultCode== RESULT_OK && data!=null){
            imageUrl= data.data!!
            imgP.setImageURI(imageUrl)
        }
    }

    private fun register(name: String, email: String, password: String,role:String,img:String,des:String,relation:String,tutorId:String) {
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(
            OnCompleteListener {
                if(it.isSuccessful){
                    val fireBaseuser: FirebaseUser? =firebaseAuth.currentUser
                    val userId:String = fireBaseuser!!.uid
                    myRef = FirebaseDatabase.getInstance().getReference("patients").child(userId)
                    var patient =Patient(userId,name,email,password,role,img,des,relation,tutorId)
                    myRef.setValue(patient).addOnCompleteListener {
                        if(it.isSuccessful) {
                            Toast.makeText(applicationContext, "Patient added", Toast.LENGTH_LONG).show()
                            val source = Intent(applicationContext, ListPatientActivity::class.java)
                            startActivity(source)
                            finish()
                        }
                    }
                }else{
                    Toast.makeText(applicationContext, "error", Toast.LENGTH_LONG).show()

                }
            })
    }

}