package com.github.taasonei.developerslife

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.github.taasonei.developerslife.databinding.ActivityMainBinding
import com.github.taasonei.developerslife.viewmodel.GifViewModel
import com.github.taasonei.developerslife.viewmodel.State

class MainActivity : AppCompatActivity() {

    private val viewModel: GifViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var circularProgressDrawable: CircularProgressDrawable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        circularProgressDrawable = CircularProgressDrawable(this)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        binding.buttonNext.setOnClickListener {
            viewModel.loadNextGif()
        }

        binding.buttonReload.setOnClickListener {
            viewModel.reloadGif()
        }

        binding.buttonPrevious.setOnClickListener {
            viewModel.loadPreviousGif()
        }

        viewModel.gif.observe(this) { state ->
            when (state) {
                is State.Success -> {
                    hideProgressBar()
                    binding.textView.text = state.data.description
                    val url = state.data.gifURL
                    if (url != null) loadImage(url) else setErrorImage()
                }
                is State.Error -> {
                    hideProgressBar()
                    binding.textView.text = ""
                    setErrorImage()
                    Toast.makeText(
                        this,
                        getString(R.string.error_message),
                        Toast.LENGTH_LONG
                    ).show()
                }
                is State.Loading -> showProgressBar()
            }
        }

        viewModel.count.observe(this) {
            binding.buttonPrevious.isEnabled = it != 0L
        }
    }

    private fun loadImage(url: String) {
        Glide.with(this)
            .load(url)
            .fitCenter()
            .placeholder(circularProgressDrawable)
            .error(R.drawable.ic_baseline_broken_image_24)
            .into(binding.imageView)
    }

    private fun setErrorImage() {
        binding.imageView.setImageDrawable(
            AppCompatResources.getDrawable(
                this,
                R.drawable.ic_baseline_broken_image_24
            )
        )
    }

    private fun hideProgressBar() {
        binding.apply {
            progressBar.visibility = View.GONE
            imageView.visibility = View.VISIBLE
            textView.visibility = View.VISIBLE
        }
    }

    private fun showProgressBar() {
        binding.apply {
            progressBar.visibility = View.VISIBLE
            imageView.visibility = View.GONE
            textView.visibility = View.GONE
        }
    }

}
