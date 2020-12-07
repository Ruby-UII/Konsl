package com.example.konsl.user.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.konsl.R
import com.example.konsl.model.Article
import com.example.konsl.model.Consultation
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HomeViewModel : ViewModel() {
    companion object{
        const val TYPE_EDUCATION = "edukasi"
        const val TYPE_TUTORIAL = "tutorial"
        const val STATUS_WAITING_FOR_CONFIRMATION = "menunggu konfirmasi"
        const val STATUS_CONFIRMED = "terkonfirmasi"
        const val STATUS_DONE = "selesai"
        const val STATUS_WAITING_FOR_CONTINUE_CONFIRMATION = "menunggu konfirmasi konsultasi lanjutan"
    }
    private val db = FirebaseFirestore.getInstance()
    private val listArticles = MutableLiveData<ArrayList<Article>>()
    private val listTutorials = MutableLiveData<ArrayList<Article>>()
    private val nextConsultationDate = MutableLiveData<String>()
    private val nextConsultationCounselor = MutableLiveData<String>()
    private val nextConsultationTime = MutableLiveData<String>()

    fun loadArticles(){
        val listItems = ArrayList<Article>()
        db.collection("articles")
            .whereEqualTo("tag", TYPE_EDUCATION)
            .limit(2)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val article = Article(
                        id = document.id,
                        title = document.data["title"] as String,
                        thumbnailUrl = document.data["thumbnail_url"] as String,
                        content = document.data["content"] as String,
                    )
                    listItems.add(article)
                    Log.d(this::class.java.simpleName, "${document.id} => ${document.data}")
                }
                listArticles.postValue(listItems)
            }
            .addOnFailureListener { exception ->
                Log.d(this::class.java.simpleName, "Error getting documents: ", exception)
            }
    }

    fun loadTutorials(){
        val listItems = ArrayList<Article>()
        db.collection("articles")
                .whereEqualTo("tag", TYPE_TUTORIAL)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val article = Article(
                                id = document.id,
                                title = document.data["title"] as String,
                                thumbnailUrl = document.data["thumbnail_url"] as String,
                                content = document.data["content"] as String,
                        )
                        listItems.add(article)
                        Log.d(this::class.java.simpleName, "${document.id} => ${document.data}")
                    }
                    listTutorials.postValue(listItems)
                }
                .addOnFailureListener { exception ->
                    Log.d(this::class.java.simpleName, "Error getting documents: ", exception)
                }
    }

    fun loadNextConsultationInfo(){
        val listItems = ArrayList<Consultation>()
        db.collection("consultations")
                .whereEqualTo("status", STATUS_CONFIRMED)
                .limit(1)
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
                        )
                        listItems.add(consultation)
                        Log.d(this::class.java.simpleName, "${document.id} => ${document.data}")
                    }
                    if(listItems.size > 0){
                        val nextConsultation = listItems[0]
                        val dateFormat = SimpleDateFormat("EEE, dd MMMM yyyy", Locale.getDefault())
                        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                        nextConsultationDate.postValue(dateFormat.format(nextConsultation.timeAccepted!!.toDate()))
                        nextConsultationCounselor.postValue(nextConsultation.counselorName)
                        nextConsultationTime.postValue(timeFormat.format(nextConsultation.timeAccepted!!.toDate()))
                    } else {
                        nextConsultationDate.postValue("-")
                        nextConsultationCounselor.postValue("-")
                        nextConsultationTime.postValue("-")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(this::class.java.simpleName, "Error getting documents: ", exception)
                }
    }

    fun getArticles() : LiveData<ArrayList<Article>> {
        return listArticles
    }

    fun getTutorials() : LiveData<ArrayList<Article>> {
        return listTutorials
    }

    fun getNextConsultationDate(): LiveData<String>{
        return nextConsultationDate
    }

    fun getNextConsultationCounselor(): LiveData<String>{
        return nextConsultationCounselor
    }

    fun getNextConsultationTime(): LiveData<String>{
        return nextConsultationTime
    }
}