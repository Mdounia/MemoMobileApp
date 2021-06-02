package com.example.alzheimerapp.controllers

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.example.alzheimerapp.R
import com.example.alzheimerapp.models.Patient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso

class ListPatientActivity : AppCompatActivity() {
    lateinit var listPatients: ListView
    lateinit var database: FirebaseDatabase
    lateinit var myRef: DatabaseReference
    private lateinit var patients: ArrayList<Patient>
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var firebaseUser: FirebaseUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_patient)
        listPatients = findViewById(R.id.lv_patient)
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("patients")
        firebaseAuth= FirebaseAuth.getInstance()
        firebaseUser= firebaseAuth.currentUser!!
        patients = ArrayList()
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                patients.clear()
                for (postSnapshot in dataSnapshot.children) {
                    val td: Map<String, Object> = postSnapshot.getValue() as HashMap<String, Object>
                    val patient = Patient(
                        td.get("patientId").toString(),
                        td.get("name").toString(),
                        td.get("email").toString(),
                        td.get("password").toString(),
                        td.get("role").toString(),
                        td.get("imgTutor").toString(),
                        td.get("des").toString(),
                        td.get("relation").toString(),
                        td.get("tutorId").toString()
                    )
                    if (patient.tutorId.equals(firebaseUser.uid)) {
                        patients.add(patient)
                    }
                    val adapter = CustomizedAdapter(this@ListPatientActivity, patients)
                    //adapter = ArrayAdapter(applicationContext, android.R.layout.simple_list_item_1, users)
                    listPatients.adapter = adapter
                }


            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

    }
    class CustomizedAdapter(private val myContext: FragmentActivity, private val patients: List<Patient>) :
        ArrayAdapter<Patient>(myContext, 0, patients) {
        override fun getView(position: Int, view: View?, parent: ViewGroup): View {
            val layout = LayoutInflater.from(myContext).inflate(R.layout.patient_row, null)
            val textViewName = layout.findViewById<View>(R.id.namePat) as TextView
            val textViewEmail = layout.findViewById<View>(R.id.emailPat) as TextView
          /* val img=layout.findViewById<View>(R.id.imgPatient) as ImageView
            Picasso.get().load(patient.imgTutor).into(img);*/
            val patient: Patient= patients.get(position)
            textViewName.setText(patient.name);
            textViewEmail.setText(patient.email);
            return layout

        }
    }
}