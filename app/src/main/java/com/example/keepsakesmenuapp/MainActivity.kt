package com.example.keepsakesmenuapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class MainActivity : AppCompatActivity() {
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db.collection("chicken")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d("ZZZZZZZZZZZ", "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w("ZZZZZZZZZZZ",    "Error getting documents.", exception)
            }
    }
}