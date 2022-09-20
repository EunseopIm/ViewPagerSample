package com.example.expprototype

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.expprototype.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnGallery.setOnClickListener {

            Intent(this, GalleryViewActivity::class.java).apply {
                startActivity(this)
            }
        }

        binding.btnViewPager.setOnClickListener {

            Intent(this, ViewPagerActivity::class.java).apply {
                startActivity(this)
            }
        }
    }
}