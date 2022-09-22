package com.example.expprototype

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.expprototype.databinding.ActivityMainBinding
import com.example.expprototype.file.FileActivity
import com.example.expprototype.gallery.GalleryViewActivity
import com.example.expprototype.service.ServiceActivity
import com.example.expprototype.viewpager.activity.ViewPagerActivity


class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {

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

        binding.btnFile.setOnClickListener {

            Intent(this, FileActivity::class.java).apply {
                startActivity(this)
            }
        }

        binding.btnService.setOnClickListener {

            Intent(this, ServiceActivity::class.java).apply {
                startActivity(this)
            }
        }
    }
}