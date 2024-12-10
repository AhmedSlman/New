package com.example.phone_extractor

import android.accessibilityservice.AccessibilityService
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import androidx.core.app.NotificationCompat
import java.util.regex.Pattern
import com.example.phone_native.R

class MyAccessibilityService : AccessibilityService() {

    companion object {
        private const val TAG = "PhoneExtractor"
        private const val CHANNEL_ID = "phone_extractor_channel"
    }

    private lateinit var notificationManager: NotificationManager

    override fun onServiceConnected() {
        super.onServiceConnected()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel()
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event?.eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
            val content = event.text.toString()
            extractPhoneNumbers(content)
        }
    }

    private fun extractPhoneNumbers(content: CharSequence) {
        // Regular expression to find phone numbers
        val phonePattern = Pattern.compile("(\\+?\\d{1,4}[\\s-]?)?(\\(?\\d{3}\\)?[\\s-]?)?[\\d\\s-]{7,10}")
        val matcher = phonePattern.matcher(content)

        while (matcher.find()) {
            val phoneNumber = matcher.group()
            Log.d(TAG, "Detected phone number: $phoneNumber")
            showNotification(phoneNumber)
        }
    }

    private fun showNotification(phoneNumber: String) {
        // Create the notification
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("رقم هاتف مكتشف")
            .setContentText("تم العثور على رقم: $phoneNumber")
            .setSmallIcon(R.drawable.ic_notification)  // Ensure this icon exists in your drawable folder
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }

    private fun createNotificationChannel() {
        // Create the notification channel for devices running Android O and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Phone Extractor Channel"
            val descriptionText = "Channel for showing detected phone numbers"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onInterrupt() {
        // No action needed when the service is interrupted
    }
}
