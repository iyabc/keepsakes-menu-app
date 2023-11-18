package com.example.keepsakesmenuapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        val etEmail = findViewById<EditText>(R.id.etLogEmail)
        val etPass = findViewById<EditText>(R.id.etLogPass)

        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnRegister = findViewById<Button>(R.id.btnRegister)

        btnLogin.setOnClickListener {
            var val_email = etEmail.text.toString()
            var val_password = etPass.text.toString()

            if(val_email.equals("") || val_password.equals("")){
                Toast.makeText(this, "Required fields.", Toast.LENGTH_LONG).show()
            }else {
                signInEmailPass(val_email, val_password)
            }
        }
    }

    override fun onStart() {
        super.onStart()

        val currentUser = auth.currentUser
        if(currentUser != null){
            updateUI(currentUser)
        }
    }

    override fun onResume() {
        super.onResume()
    }

    fun updateUI(user: FirebaseUser?){

        if(user != null){
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }else {
            Toast.makeText(
                baseContext,
                "You did not login.",
                Toast.LENGTH_SHORT,
            ).show()
        }

    }

    fun signInEmailPass(email:String, passw:String){
        var TAG = "ZZZTagFirebase"

        auth.signInWithEmailAndPassword(email, passw)
            .addOnCompleteListener(this){task ->
                if(task.isSuccessful){
                    Log.d(TAG, "signInWithEmail: success")
                    val user = auth.currentUser

                    Toast.makeText(
                        baseContext,
                        "Authentication successful.",
                        Toast.LENGTH_SHORT
                    ).show()

                    updateUI(user)
                }else {
                    Log.d(TAG, "signInWithEmail: failed", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

}