package com.example.konsl.user.ui.consultations

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.konsl.model.Consultation
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class ConsultationsViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val mAuth = FirebaseAuth.getInstance()
    private val listConsultations = MutableLiveData<ArrayList<Consultation>>()

    fun loadConsultations(){
        val listItems = ArrayList<Consultation>()

        db.collection("consultations")
                .whereEqualTo("user_id", mAuth.uid)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val consultation = Consultation(
                                id = document.id,
                                userName = document.data["user_name"] as String,
                                userId = document.data["user_id"] as String,
                                problem = document.data["problem"] as String,
                                effort = document.data["effort"] as String,
                                obstacle = document.data["obstacle"] as String,
                                status = document.data["status"] as String,
                                timeRequest = document.data["time_request"] as String,
                                genderRequest = document.data["gender_request"] as String,
                                createdAt = document.data["created_at"] as Timestamp,
                                timeAccepted = document.data["time_accepted"] as Timestamp?,
                                counselorId = document.data["counselor_id"] as String?,
                                counselorName = document.data["counselor_name"] as String?,
                        )
                        listItems.add(consultation)
                        Log.d(this::class.java.simpleName, "${document.id} => ${document.data}")
                    }
                    listConsultations.postValue(listItems)
                }
                .addOnFailureListener { exception ->
                    Log.d(this::class.java.simpleName, "Error getting documents: ", exception)
                }
    }

    fun getConsultations() : LiveData<ArrayList<Consultation>> {
        return listConsultations
    }
}