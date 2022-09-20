package com.example.expprototype

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.expprototype.databinding.ActivityMainBinding
import com.example.expprototype.databinding.ActivityViewPagerBinding

class ViewPagerActivity : AppCompatActivity() {

    private val binding: ActivityViewPagerBinding by lazy {
        ActivityViewPagerBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }
}