package com.example.konsl.psychologist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.konsl.LoginActivity
import com.example.konsl.R
import com.example.konsl.adapter.ConsultationSectionsPagerAdapter
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_psychologist_home.*

class PsychologistHomeActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_psychologist_home)

        mAuth = FirebaseAuth.getInstance()

        val consultationSectionsPagerAdapter = ConsultationSectionsPagerAdapter(this, supportFragmentManager)
        viewPagerConsultation.adapter = consultationSectionsPagerAdapter
        tabsConsultation.setupWithViewPager(viewPagerConsultation)

        supportActionBar?.elevation = 0f
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menuLogout){
            mAuth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}