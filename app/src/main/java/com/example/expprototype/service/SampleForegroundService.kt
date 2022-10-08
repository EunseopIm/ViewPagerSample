package com.example.expprototype.service

import android.app.*
import android.app.ActivityManager.RunningAppProcessInfo
import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.expprototype.MainActivity
import com.example.expprototype.R
import com.example.expprototype.service.ServiceActivity.Companion.ACTION_STOP
import java.text.SimpleDateFormat
import java.util.*


class SampleForegroundService : Service() {

    //Notififcation for ON-going
    private var iconNotification: Bitmap? = null
    private var notification: Notification? = null
    var mNotificationManager: NotificationManager? = null
    private val mNotificationId = 123

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {


        Log.v(">>>", "@# onStartCommand")
        if (intent?.action != null && intent.action.equals(ACTION_STOP, ignoreCase = true)) {
            stopForeground(true)
            stopSelf()
        }

        //START_NOT_STICKY: 서비스를 명시적으로 다시 시작할 때 까지 만들지 않습니다.
        //START_STICKY: 서비스를 다시 만들지만 마지막 Intent를 onStartCommand의 인자로 다시 전달하지 않습니다. 이는 일단 계속 살아있어야되지만 별다른 동작이 필요하지 않은 음악앱같은 서비스에 적절합니다.
        //START_REDELIVER_INTENT: 이름에서 알겠듯이 마지막 Intent를 onStartCommand의 인자로 다시 전달해줍니다. 즉각적인 반응이 필요한 파일 다운로드 서비스 같은 곳에 적합합니다.

        generateForegroundNotification()
        checkActivity()

        return START_NOT_STICKY
    }

    var stopFlag = false
    override fun onDestroy() {
        super.onDestroy()
    }

    private fun generateForegroundNotification() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val intentMainLanding = Intent(this, MainActivity::class.java)
            val pendingIntent =
                PendingIntent.getActivity(this, 0, intentMainLanding, PendingIntent.FLAG_MUTABLE)
            iconNotification = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
            if (mNotificationManager == null) {
                mNotificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                assert(mNotificationManager != null)
                mNotificationManager?.createNotificationChannelGroup(
                    NotificationChannelGroup("chats_group", "Chats")
                )
                val notificationChannel =
                    NotificationChannel("service_channel", "Service Notifications",
                        NotificationManager.IMPORTANCE_MIN)
                notificationChannel.enableLights(false)
                notificationChannel.lockscreenVisibility = Notification.VISIBILITY_SECRET
                mNotificationManager?.createNotificationChannel(notificationChannel)
            }
            val builder = NotificationCompat.Builder(this, "service_channel")

            builder.setContentTitle(StringBuilder(resources.getString(R.string.app_name)).append(" service is running").toString())
                .setTicker(StringBuilder(resources.getString(R.string.app_name)).append("service is running").toString())
                .setContentText("Touch to open") //                    , swipe down for more options.
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setWhen(0)
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
            if (iconNotification != null) {
                builder.setLargeIcon(Bitmap.createScaledBitmap(iconNotification!!, 128, 128, false))
            }
            builder.color = resources.getColor(R.color.purple_200)
            notification = builder.build()
            startForeground(mNotificationId, notification)
        }
    }

    var id = 1001
    private fun generateForegroundNotification2() {

        Log.v(">>>", "@# generateForegroundNotification2")
        val intentMainLanding = Intent(this, MainActivity::class.java)
        val notifyPendingIntent =
            PendingIntent.getActivity(this, 0, intentMainLanding, PendingIntent.FLAG_MUTABLE)

        val builder: NotificationCompat.Builder
        val mNotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val CHANNEL_ID = getString(R.string.app_name)
            val channel = NotificationChannel(
                CHANNEL_ID,
                getString(R.string.app_name),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.setShowBadge(false)
            mNotificationManager.createNotificationChannel(channel)
            builder = NotificationCompat.Builder(this, CHANNEL_ID)
            builder.setDefaults(Notification.DEFAULT_LIGHTS or Notification.DEFAULT_SOUND)
                .setAutoCancel(true)
                .setVibrate(longArrayOf(0L))
            builder.setNumber(0)
        } else {
            builder = NotificationCompat.Builder(this)
            builder.setAutoCancel(true)
        }

        builder.setSmallIcon(R.mipmap.ic_launcher)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("title")
            .setContentText("message")
            .setContentIntent(notifyPendingIntent)
        mNotificationManager.notify(id++, builder.build())
    }

    private val runnable = {

        try {

            val manager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
            val info = manager.getRunningTasks(1)
            val componentName = info[0].topActivity
            val topActivityName = componentName!!.shortClassName.substring(1)

            generateForegroundNotification2()
            Log.v(">>>", "@# Check : ${topActivityName}")
            Log.v(">>>", "@# Check2 : ${getForegroundActivity()}")
            Log.v(">>>", "@# Check3 : ${getForegroundApp(applicationContext)}")
            Log.v(">>>", "@# Check4 : ${getForegroundApp2(applicationContext)}")

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

        checkActivity()
    }

    var first = false
    private fun checkActivity() {

        if (stopFlag) return
        val secondDelay = 1000 * 15
        val firstDelay = 1000 * 15
        var delay = if (first) secondDelay else firstDelay

        Handler(Looper.getMainLooper()).postDelayed(runnable, 1000 * 15)
    }

    private fun getForegroundActivity(): String? {

        val mActivityManager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        if (mActivityManager.getRunningTasks(1) == null) {
            Log.e(">>>", "running task is null, ams is abnormal!!!")
            return null
        }
        val mRunningTask = mActivityManager.getRunningTasks(1)[0]
        if (mRunningTask == null) {
            Log.e(">>>", "failed to get RunningTaskInfo")
            return null
        }
        //String activityName =  mRunningTask.topActivity.getClassName();
        return mRunningTask.topActivity!!.packageName
    }

    private fun getForegroundApp(context: Context): String? {

        val am = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val lr = am.runningAppProcesses ?: return null
        for (ra in lr) {
            if (ra.importance == RunningAppProcessInfo.IMPORTANCE_VISIBLE
                || ra.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND
            ) {
                return ra.processName
            }
        }
        return null
    }

    private val sdf = SimpleDateFormat("(dd) HH:mm:ss")

    var isInit = true
    private fun getForegroundApp2(context: Context): String? {

        Log.v(">>>", "getForegroundApp2!!!!!Main")
        val usageStatsManager = context.getSystemService(USAGE_STATS_SERVICE) as UsageStatsManager
        val ts = System.currentTimeMillis()
        val queryUsageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, 0, ts)
        val usageEvents = usageStatsManager.queryEvents(if (isInit) 0 else ts - 5000, ts) ?: return null
        val event = UsageEvents.Event()
        var lastEvent: UsageEvents.Event? = null

        Log.v(">>>", "getForegroundApp2 size : ${usageEvents.getNextEvent(event)}")
        while (usageEvents.getNextEvent(event)) {
            // if from notification bar, class name will be null
            if (event.packageName == null || event.className == null) {
                continue
            }

            if (lastEvent == null || lastEvent.timeStamp < event.timeStamp) {
                lastEvent = event
            }

            val calendar = Calendar.getInstance()
            calendar.timeInMillis = event.timeStamp

            val lastCalendar = Calendar.getInstance()
            lastCalendar.timeInMillis = lastEvent.timeStamp

            if (event.eventType == UsageEvents.Event.ACTIVITY_RESUMED) {
                Log.v(">>>", "#@ Name[${event.packageName}], Time[${sdf.format(calendar.time)}] LastTime[${sdf.format(lastCalendar.time)}]")
            }
        }
        return lastEvent?.packageName
    }
}