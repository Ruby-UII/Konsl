package com.example.konsl.user.ui.educations

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.konsl.model.Article
import com.google.firebase.firestore.FirebaseFirestore

class EducationsViewModel : ViewModel() {
    companion object{
        const val TYPE_EDUCATION = "edukasi"
    }

    private val db = FirebaseFirestore.getInstance()
    private val listArticles = MutableLiveData<ArrayList<Article>>()

    fun setArticles(){
        val listItems = ArrayList<Article>()

        db.collection("articles")
            .whereEqualTo("tag", TYPE_EDUCATION)
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

    fun getArticles() : LiveData<ArrayList<Article>> {
        return listArticles
    }
}