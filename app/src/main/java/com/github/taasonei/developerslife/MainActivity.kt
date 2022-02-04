package com.github.taasonei.developerslife

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.google.android.material.textview.MaterialTextView

class MainActivity : AppCompatActivity() {

    private val viewModel: GifViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val imageView = findViewById<ImageView>(R.id.image_view)
        val textView = findViewById<MaterialTextView>(R.id.text_view)
        val circularProgressDrawable = CircularProgressDrawable(this)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        viewModel.gif.observe(this, Observer {
            textView.text = it.description

            Glide.with(this)
                .load(it.gifURL)
                .centerCrop()
                .placeholder(circularProgressDrawable)
                .error(R.drawable.ic_baseline_broken_image_24)
                .into(imageView)
        })
    }

}
