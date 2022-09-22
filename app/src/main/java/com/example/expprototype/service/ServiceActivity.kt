package com.example.expprototype.service

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.expprototype.BuildConfig
import com.example.expprototype.databinding.ActivityServiceBinding

class ServiceActivity : AppCompatActivity() {

    private val binding: ActivityServiceBinding by lazy {
        ActivityServiceBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnStart.setOnClickListener {

            Log.v(">>>", "@# Start")
            startService(Intent(this, SampleForegroundService::class.java))
            updateTextStatus()
        }

        binding.btnStop.setOnClickListener {

            Log.v(">>>", "@# Stop")
            val intentStop = Intent(this, SampleForegroundService::class.java)
            intentStop.action = ACTION_STOP
            startService(intentStop)
            Handler(Looper.getMainLooper()).postDelayed({
                updateTextStatus()
            },100)
        }

        updateTextStatus()
    }

    private fun updateTextStatus() {

        if(isMyServiceRunning(SampleForegroundService::class.java)){
            binding.tvStatus.text = "Service is Running"
        }else{
            binding.tvStatus.text = "Service is NOT Running"
        }
    }


    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {

        try {
            val manager =
                getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            for (service in manager.getRunningServices(
                Int.MAX_VALUE
            )) {
                if (serviceClass.name == service.service.className) {
                    return true
                }
            }
        } catch (e: Exception) {
            return false
        }
        return false
    }



    companion object{
        const val  ACTION_STOP = "${BuildConfig.APPLICATION_ID}.stop"
    }
}