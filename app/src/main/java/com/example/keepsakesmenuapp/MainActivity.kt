package com.example.keepsakesmenuapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    lateinit var btnLogout: Button
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Initialize Firebase Auth
        auth = Firebase.auth

        btnLogout = findViewById(R.id.btnLogout)

        btnLogout.setOnClickListener {
            Firebase.auth.signOut()
            finish()
        }

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

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser === null) {
            Log.d("ZZZZZZ", "No user logged in.")
            val loginScreen = Intent (this, LoginActivity::class.java)
            startActivity(loginScreen)
        }
    }
}