package com.example.expprototype.gallery

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.expprototype.databinding.ActivityGalleryViewBinding

class GalleryViewActivity : AppCompatActivity() {

    private val binding: ActivityGalleryViewBinding by lazy {
        ActivityGalleryViewBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }
}