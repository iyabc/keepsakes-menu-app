package com.example.keepsakesmenuapp

import android.app.ActionBar.LayoutParams
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Space
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore


class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    lateinit var btnLogout: Button
    lateinit var btnAddItem: Button
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Initialize Firebase Auth
        auth = Firebase.auth

        btnLogout = findViewById<Button>(R.id.btnLogout)
        btnAddItem = findViewById<Button>(R.id.btnAddItem)

        btnLogout.setOnClickListener {
            Firebase.auth.signOut()
            finish()
            var intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        btnAddItem.setOnClickListener {
            finish()
            var intent = Intent(this, AddItemActivity::class.java)
            startActivity(intent)
        }

        db.collection("items")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d("ZZZ", "${document.id} => ${document.data}")

                    val itemName = document.data["name"].toString()
                    val imgArray = document.data["photoUrl"].toString().replace("[", "").replace("]", "").split(", ")
                    val price = String.format("P %.2f", document.data["price"].toString().toFloat())
                    val description = document.data["description"].toString()

                    if(imgArray != null){
                        Log.d("ZZZ", "${imgArray}")
                        Log.d("ZZZ", "Image one: ${imgArray[0]}")
                        Log.d("ZZZ", "Image two: ${imgArray[1]}")
                    }



                    val listLayout = findViewById<LinearLayout>(R.id.listLayout)

                    val cardView = CardView(this)
                    cardView.setCardBackgroundColor(resources.getColor(R.color.light_pink, null))
                    cardView.radius = 30F
                    cardView.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT).apply {
                        setMargins(0, 0, 0, 60)
                    }

                    val cardLayout = LinearLayout(this)
                    cardLayout.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
                    cardLayout.orientation = LinearLayout.HORIZONTAL
                    cardLayout.setPadding(
                        resources.getDimensionPixelOffset(R.dimen.padding_20),
                        resources.getDimensionPixelOffset(R.dimen.padding_20),
                        resources.getDimensionPixelOffset(R.dimen.padding_20),
                        resources.getDimensionPixelOffset(R.dimen.padding_20))

                    val textLayout = LinearLayout(this)
                    textLayout.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
                    textLayout.orientation = LinearLayout.VERTICAL
                    textLayout.gravity = Gravity.START
                    textLayout.setPadding(
                        resources.getDimensionPixelOffset(R.dimen.padding_20),
                        resources.getDimensionPixelOffset(R.dimen.padding_10),
                        resources.getDimensionPixelOffset(R.dimen.padding_20),
                        resources.getDimensionPixelOffset(R.dimen.padding_10),
                    )

                    val imgView = ImageView(this)
                    imgView.layoutParams = LayoutParams(
                        resources.getDimensionPixelOffset(R.dimen.menuImage_dimen),
                        resources.getDimensionPixelOffset(R.dimen.menuImage_dimen))
                    imgView.setImageDrawable(resources.getDrawable(R.drawable.adobo_flakes, null))
                    Glide.with(this).load(imgArray[0]).into(imgView)

                    val titleTxtView = TextView(this)
                    titleTxtView.text = (itemName.split(" ").joinToString(" "){
                        it.replaceFirstChar(Char::uppercaseChar)
                    })
                    titleTxtView.textSize = 16F
                    titleTxtView.setTypeface(null, Typeface.BOLD)
                    titleTxtView.setTextColor(resources.getColor(R.color.dark_pink, null))

                    val priceTxtView = TextView(this)
                    priceTxtView.text = (price)
                    priceTxtView.textSize = 16F
                    priceTxtView.setTextColor(resources.getColor(R.color.dark_pink, null))

                    val spaceBetween = Space(this)
                    spaceBetween.layoutParams = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT,1F)

                    val viewDetailsTxtView = TextView(this)
                    val descText = SpannableString("View Detail")
                    descText.setSpan(UnderlineSpan(), 0, descText.length, 0)
                    viewDetailsTxtView.text = (descText)
                    viewDetailsTxtView.textSize = 14F
                    viewDetailsTxtView.setTextColor(resources.getColor(R.color.dark_pink, null))
                    viewDetailsTxtView.setTypeface(null, Typeface.BOLD)


                    viewDetailsTxtView.setOnClickListener {
                        val viewDetailsActivity = Intent(this, ViewDetailsActivity::class.java)
                        viewDetailsActivity.putExtra("img1", imgArray[0])
                        viewDetailsActivity.putExtra("img2", imgArray[1])
                        viewDetailsActivity.putExtra("name", itemName)
                        viewDetailsActivity.putExtra("price", price)
                        viewDetailsActivity.putExtra("description", description)
                        startActivity(viewDetailsActivity)
                    }

                    textLayout.addView(titleTxtView)
                    textLayout.addView(priceTxtView)
                    textLayout.addView(spaceBetween)
                    textLayout.addView(viewDetailsTxtView)
                    cardLayout.addView(imgView)
                    cardLayout.addView(textLayout)
                    cardView.addView(cardLayout)
                    listLayout.addView(cardView)

                }
            }
            .addOnFailureListener { exception ->
                Log.w("ZZZ",    "Error getting documents.", exception)
            }
    }

    public override fun onStart() {
        super.onStart()

        val currentUser = auth.currentUser
        if (currentUser === null) {
            Log.d("ZZZZZZ", "No user logged in.")
            val loginScreen = Intent (this, LoginActivity::class.java)
            startActivity(loginScreen)
        }
    }
}