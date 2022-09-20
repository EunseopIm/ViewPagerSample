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
import com.example.expprototype.databinding.ActivityViewPagerBinding

class FileActivity : AppCompatActivity() {

    private val binding: ActivityViewPagerBinding by lazy {
        ActivityViewPagerBinding.inflate(layoutInflater)
    }


    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        printLog()
        initPermission()
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

    @RequiresApi(Build.VERSION_CODES.N)
    private fun printLog() {

        val TAG = ">>>"

        // Internal storage
        Log.d(TAG, "Internal private data dir: ${dataDir.absolutePath}")
        Log.d(TAG, "Internal private file dir: ${filesDir.absolutePath}")
        Log.d(TAG, "Internal private cache dir: ${cacheDir.absolutePath}")

        // External app's storage
        Log.d(TAG, "External app's cache dir: ${externalCacheDir?.absolutePath}")
        Log.d(TAG, "External app's file dir: ${getExternalFilesDir(Environment.DIRECTORY_PICTURES)}")

        // External public storage
        Log.d(TAG, "External public root dir: ${Environment.getExternalStorageDirectory()}")
        Log.d(TAG, "External public file dir: ${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)}")
    }
}