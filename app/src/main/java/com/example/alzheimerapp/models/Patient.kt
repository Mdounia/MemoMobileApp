package com.example.alzheimerapp.models

import java.io.Serializable

class Patient(
    var patientId:String,
    var name: String,
    var email: String,
    var password: String,
    var role:String,
    var imgTutor:String,
    var des:String,
    var relation:String,
    var tutorId:String
):Serializable {
}