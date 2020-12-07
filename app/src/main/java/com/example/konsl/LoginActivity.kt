package com.example.konsl

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.konsl.user.UserHomeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private val ROLE_USER = "user"
    private val ROLE_ADMIN = "admin"
    private val ROLE_PSYCHOLOGIST = "psychologist"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()

        supportActionBar?.hide()
    }

    fun signUp(view: View) {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    fun login(view: View) {
        if(isValid()){
            mAuth.signInWithEmailAndPassword(etEmail.text.toString(), etPassword.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = mAuth.currentUser
                        if(user != null){
                            toHomeActivity(user)
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        btnLogin.isEnabled = true
                        btnLogin.text = getString(R.string.login)
                        Toast.makeText(this, getString(R.string.login_failed), Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun toHomeActivity(currentUser: FirebaseUser) {
        // check user role
        val db = FirebaseFirestore.getInstance()

        db.collection("users")
            .whereEqualTo("auth_id", currentUser.uid)
            .get()
            .addOnSuccessListener { documents ->
                // to home activity
                Toast.makeText(this, getString(R.string.login_success), Toast.LENGTH_SHORT).show()
                var role : String = ""
                for (document in documents){
                    role = document.data["role"] as String
                }
                if (role == ROLE_USER){
                    val intent = Intent(this, UserHomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            .addOnFailureListener { exception ->
                Log.w(this::class.java.simpleName, "Error getting documents: ", exception)
            }
    }

    private fun isValid(): Boolean {
        if(etEmail.text.toString().isNotEmpty() && etPassword.text.toString().isNotEmpty()
        ){
            btnLogin.isEnabled = false
            btnLogin.text = getString(R.string.wait_a_moment)
            return true
        }
        Toast.makeText(
                this,
                getString(R.string.please_input_correctly),
                Toast.LENGTH_SHORT
        ).show()
        return false
    }
}