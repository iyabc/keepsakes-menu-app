package com.example.keepsakesmenuapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.firestore

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore

    @IgnoreExtraProperties
    data class User(val displayName: String, val email: String? = null)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = Firebase.auth

        val etDisplayName = findViewById<EditText>(R.id.etRegisterDisplayName)
        val etEmail = findViewById<EditText>(R.id.etRegisterEmail)
        val etPassword = findViewById<EditText>(R.id.etRegisterPassword)

        val registerLoginBtn = findViewById<Button>(R.id.registerLoginBtn)

        registerLoginBtn.setOnClickListener {
            var val_displayName = etDisplayName.text.toString()
            var val_email = etEmail.text.toString()
            var val_password = etPassword.text.toString()

            if(val_displayName.equals("") || val_email.equals("") || val_password.equals("")){
                Toast.makeText(
                    baseContext,
                    "Required fields.",
                    Toast.LENGTH_SHORT,
                ).show()
            }else {
                registerUser(val_displayName, val_email, val_password)
            }
        }
    }

    fun updateUI(user: FirebaseUser?){
        if(user != null){
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }else {
            Toast.makeText(
                baseContext,
                "Login failed.",
                Toast.LENGTH_SHORT,
            ).show()
        }
    }

    fun addNewUser(displayName: String, email: String) {
        val user = User(displayName, email)

        db.collection("users")
            .add(user)
            .addOnSuccessListener{
                Log.d("ZZZZZZZZZtodatabase", "User added to database.")
            }
            .addOnFailureListener{
                Log.d("ZZZZZZZZZtodatabase", "User not added to database.")
            }
    }

    fun registerUser(displayName: String, email: String, password: String){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        baseContext,
                        "Registration success.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    val user = auth.currentUser
                    addNewUser(displayName, email.lowercase())
                    updateUI(user)
                } else {
                    var errorMessage = task.exception?.message
                    ?: "Authentication failed. ${task.exception?.localizedMessage}"

                    if (errorMessage.contains("INVALID_LOGIN_CREDENTIALS")) {
                        errorMessage = "Invalid login credentials."
                    }

                    Toast.makeText(
                        baseContext,
                        "$errorMessage",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }
}