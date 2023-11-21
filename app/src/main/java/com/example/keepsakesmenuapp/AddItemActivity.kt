package com.example.keepsakesmenuapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.firestore
import com.google.type.Date
import java.time.LocalDate

class AddItemActivity : AppCompatActivity(){
    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore
    lateinit var btnAddItemSubmit: Button

    @IgnoreExtraProperties
    data class Item(
        val name: String,
        val description: String,
        val price: String,
        val dateCreated: Timestamp,
        val photoUrl: List<String>
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        auth = Firebase.auth

        val etItemName = findViewById<EditText>(R.id.etItemName)
        val etDescription = findViewById<EditText>(R.id.etDescription)
        val etPrice = findViewById<EditText>(R.id.etPrice)
        val etImageUrlFirst = findViewById<EditText>(R.id.etImageUrlFirst)
        val etImageUrlSecond = findViewById<EditText>(R.id.etImageUrlSecond)

        btnAddItemSubmit = findViewById<Button>(R.id.btnAddItemSubmit)

        btnAddItemSubmit.setOnClickListener {
            val val_itemName = etItemName.text.toString()
            val val_description = etDescription.text.toString()
            val val_price = etPrice.text.toString()
            val val_timestamp = Timestamp.now()
            val val_imageUrlFirst = etImageUrlFirst.text.toString()
            val val_imageUrlSecond = etImageUrlSecond.text.toString()
            val val_imagesUrlArray = listOf(val_imageUrlFirst, val_imageUrlSecond)

            if(val_itemName.equals("") || val_description.equals("") || val_price.equals("") || val_imageUrlFirst.equals("") || val_imageUrlSecond.equals("")){
                Toast.makeText(
                    baseContext,
                    "Required fields.",
                    Toast.LENGTH_SHORT,
                ).show()
            }else {
                Log.d("ZZZZZZZZZZZzz", val_imagesUrlArray[0].toString())
                Log.d("ZZZZZZZZZZZzz", val_imagesUrlArray[1].toString())
                var item = Item(val_itemName, val_description, val_price,val_timestamp, val_imagesUrlArray)
                addItem(item)
            }
        }
    }

    fun updateUI(){
        var intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun addItem(item: Item){
        var TAG = "ZZZZZZadditem"

        db.collection("items")
            .add(item)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(
                    baseContext,
                    "${item.name} added.",
                    Toast.LENGTH_SHORT,
                ).show()
                updateUI()
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    baseContext,
                    "${e.message}",
                    Toast.LENGTH_SHORT,
                ).show()
            }
    }
}