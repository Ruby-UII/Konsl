package com.example.konsl

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.example.konsl.psychologist.PsychologistHomeActivity
import com.example.konsl.user.UserHomeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var handler: Handler
    private val ROLE_USER = "user"
    private val ROLE_ADMIN = "admin"
    private val ROLE_PSYCHOLOGIST = "psikolog"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        mAuth = FirebaseAuth.getInstance()

        handler = Handler()
        handler.postDelayed({
            changeActivity()
        }, 2000)
    }

    private fun changeActivity() {
        val currentUser: FirebaseUser? = mAuth.currentUser
        if(currentUser != null){
            // check user role
            // to home activity
            val db = FirebaseFirestore.getInstance()

            db.collection("users")
                .whereEqualTo("auth_id", currentUser.uid)
                .get()
                .addOnSuccessListener { documents ->
                    var role = ""
                    for (document in documents){
                        role = document.data["role"] as String
                    }
                    if (role == ROLE_USER){
                        val intent = Intent(this, UserHomeActivity::class.java)
                        startActivity(intent)
                    }
                    else if(role == ROLE_PSYCHOLOGIST){
                        val intent = Intent(this, PsychologistHomeActivity::class.java)
                        startActivity(intent)
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w(this::class.java.simpleName, "Error getting documents: ", exception)
                }
        } else {
            // to login activity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}

