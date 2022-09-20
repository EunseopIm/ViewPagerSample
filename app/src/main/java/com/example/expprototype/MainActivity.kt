package com.example.expprototype

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.expprototype.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initPermission()
        initView()
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun initPermission() {

        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.MANAGE_EXTERNAL_STORAGE), 1)
        if (!Environment.isExternalStorageManager()) {

            val intent = Intent()
            intent.action = Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
            val uri: Uri = Uri.fromParts("package", this.packageName, null)
            intent.data = uri
            startActivity(intent)
        }
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
    }
}