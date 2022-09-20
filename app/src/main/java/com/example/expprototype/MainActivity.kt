package com.example.expprototype

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.expprototype.databinding.ActivityMainBinding


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
    }
}