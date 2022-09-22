package com.example.expprototype.service

import android.Manifest
import android.app.ActivityManager
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Process
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.expprototype.BuildConfig
import com.example.expprototype.databinding.ActivityServiceBinding


class ServiceActivity : AppCompatActivity() {

    companion object{
        const val  ACTION_STOP = "${BuildConfig.APPLICATION_ID}.stop"
    }

    private val binding: ActivityServiceBinding by lazy {
        ActivityServiceBinding.inflate(layoutInflater)
    }

    private fun checkForPermission(): Boolean {
        val appOps = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, Process.myUid(), packageName)
        return mode == AppOpsManager.MODE_ALLOWED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (!checkForPermission()) {
            startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
        } else {
            /*val usageStats = getAppUsageStats()
            showAppUsageStats(usageStats)*/
        }

        binding.btnStart.setOnClickListener {

            startService(Intent(this, SampleForegroundService::class.java))
            updateTextStatus()
        }

        binding.btnStop.setOnClickListener {

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


    @SuppressWarnings("deprecation")
    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {

        try {
            val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
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
}