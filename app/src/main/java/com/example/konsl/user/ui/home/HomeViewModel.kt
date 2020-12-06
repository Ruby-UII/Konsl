package com.example.konsl.user.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.konsl.model.Article
import com.google.firebase.firestore.FirebaseFirestore

class HomeViewModel : ViewModel() {
    companion object{
        const val TYPE_EDUCATION = "edukasi"
        const val TYPE_TUTORIAL = "tutorial"
    }
    private val db = FirebaseFirestore.getInstance()
    private val listArticles = MutableLiveData<ArrayList<Article>>()
    private val listTutorials = MutableLiveData<ArrayList<Article>>()

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

    fun getArticles() : LiveData<ArrayList<Article>> {
        return listArticles
    }

    fun getTutorials() : LiveData<ArrayList<Article>> {
        return listTutorials
    }
}