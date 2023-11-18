package com.example.keepsakesmenuapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class ViewDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_details)


        val thisIntent: Intent = intent
        val name: String = thisIntent.getStringExtra("name").toString()
        val price: String = thisIntent.getStringExtra("price").toString()
        val description: String = thisIntent.getStringExtra("description").toString()
        val img1Url: String = thisIntent.getStringExtra("img1").toString()
        val img2Url: String = thisIntent.getStringExtra("img2").toString()

        val img1View = findViewById<ImageView>(R.id.img1)
        Glide.with(this).load(img1Url).into(img1View)

        val img2View = findViewById<ImageView>(R.id.img2)
        Glide.with(this).load(img2Url).into(img2View)

        val itemNameView = findViewById<TextView>(R.id.itemName)
        itemNameView.text = name.split(" ").joinToString(" "){
            it.replaceFirstChar(Char::uppercaseChar)
        }

        val itemPriceView = findViewById<TextView>(R.id.itemPrice)
        itemPriceView.text = price

        val itemDescView = findViewById<TextView>(R.id.itemDesc)
        itemDescView.text = description

        val backBtn = findViewById<Button>(R.id.backBtn)

        backBtn.setOnClickListener {
            finish()
        }



    }
}