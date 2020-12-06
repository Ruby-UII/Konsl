package com.example.konsl

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.konsl.user.UserHomeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.etEmail
import kotlinx.android.synthetic.main.activity_register.etPassword
import java.text.SimpleDateFormat
import java.util.*


class RegisterActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private val ROLE_USER = "user"
    private val ROLE_ADMIN = "admin"
    private val ROLE_PSYCHOLOGIST = "psychologist"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mAuth = FirebaseAuth.getInstance()
    }

    fun signIn(view: View) {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = intent.flags ?: Intent.FLAG_ACTIVITY_NO_HISTORY
        startActivity(intent)
    }

    fun register(view: View) {
        if(isValid()){
            mAuth.createUserWithEmailAndPassword(
                etEmail.text.toString(),
                etPassword.text.toString()
            )
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = mAuth.currentUser
                        if(user != null){
                            saveUser(user)
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        btnRegister.isEnabled = true
                        btnRegister.text = getString(R.string.register)
                        Toast.makeText(this, "Register failed.", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun isValid(): Boolean {
        if( etName.text.toString().isNotEmpty()
            && rgGender.checkedRadioButtonId != -1
            && etEmail.text.toString().isNotEmpty()
            && etPhoneNumber.toString().isNotEmpty()
            && etBirthPlace.toString().isNotEmpty()
            && etBirthDate.toString().isNotEmpty()
            && etHobby.toString().isNotEmpty()
            && etAddress.toString().isNotEmpty()
            && etPassword.text.toString().isNotEmpty()
            && etPasswordConfirmation.text.toString().isNotEmpty()
            && etPassword.text.toString() == etPasswordConfirmation.text.toString()
        ){
            btnRegister.isEnabled = false
            btnRegister.text = "Tunggu sebentar..."
            return true
        }
        Toast.makeText(
            this,
            "Pendaftaran gagal. Harap memasukkan inputan dengan benar.",
            Toast.LENGTH_SHORT
        ).show()
        return false
    }

    private val myCalendar: Calendar = Calendar.getInstance()

    var date =
        OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            myCalendar[Calendar.YEAR] = year
            myCalendar[Calendar.MONTH] = monthOfYear
            myCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth
            updateBirthDateLabel()
        }

    fun showDatePicker(view: View) {
        DatePickerDialog(
            this, date, myCalendar[Calendar.YEAR],
            myCalendar[Calendar.MONTH],
            myCalendar[Calendar.DAY_OF_MONTH]
        ).show()
    }

    private fun updateBirthDateLabel(){
        val myFormat = "dd/MM/yy" //In which you need put here

        val sdf = SimpleDateFormat(myFormat, Locale.US)

        etBirthDate.setText(sdf.format(myCalendar.time))
    }

    private fun saveUser(authUser: FirebaseUser){
        val db = FirebaseFirestore.getInstance()

        val user: MutableMap<String, Any> = HashMap()
        user["auth_id"] = authUser.uid
        user["name"] = etName.text.toString()
        val genderChecked: RadioButton = findViewById(rgGender.checkedRadioButtonId)
        user["gender"] = genderChecked.text.toString()
        user["phone_number"] = etPhoneNumber.text.toString()
        user["birth_place"] = etBirthPlace.text.toString()
        user["birth_date"] = etBirthDate.text.toString()
        user["hobby"] = etHobby.text.toString()
        user["address"] = etAddress.text.toString()
        user["role"] = ROLE_USER

        db.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(this, "Register Success.", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, UserHomeActivity::class.java)
                startActivity(intent)
                Log.d(this::class.java.simpleName, "DocumentSnapshot added with ID: " + documentReference.id)
            }
            .addOnFailureListener { e -> Log.w(this::class.java.simpleName, "Error adding document", e) }
    }
}