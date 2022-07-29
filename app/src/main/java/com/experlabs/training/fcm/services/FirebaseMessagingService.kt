package com.experlabs.training.fcm.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_MUTABLE
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import androidx.core.content.ContextCompat
import com.experlabs.training.R
import com.experlabs.training.activities.MainActivity
import com.experlabs.training.fcm.RetrofitInstance
import com.experlabs.training.fcm.models.NotificationData
import com.experlabs.training.fcm.models.PushNotification
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FirebaseMessagingService : FirebaseMessagingService() {
    private lateinit var builder: Notification.Builder
    private val channel = "Meme-Notifications"
    private val description = "memeNotifications"
    lateinit var intent: Intent


    override fun onNewToken(p0: String) {
        super.onNewToken(p0)

        addTokenToFirestore(p0)
    }

    companion object {
        fun addTokenToFirestore(newRegistrationToken: String?) {
            if (newRegistrationToken == null) throw NullPointerException("FCM token is null.")

            FirebaseDatabase.getInstance().getReference("users").let { db ->
                db.child("$newRegistrationToken")
                    .get().addOnSuccessListener {
                        if (!it.exists()) {
                            db.child(newRegistrationToken).setValue(newRegistrationToken)
                            CoroutineScope(Dispatchers.IO).launch{
                                try {
                                    val response =
                                        RetrofitInstance.api.postNotification(PushNotification(NotificationData("Welcome", "Enjoy memes on this platform!", "0"),newRegistrationToken))
                                    if (response.isSuccessful) {
                                        Log.d(ContentValues.TAG, "Response: ${Gson().toJson (response)}")
                                    } else {
                                        Log.e(ContentValues.TAG, response.errorBody().toString())
                                    }
                                } catch (e: Exception) {
                                    Log.e(ContentValues.TAG, e.toString())
                                }
                            }
                        }
                    }
            }
        }

    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        sendNotification(
            message.data["body"] ?: "Body",
            message.data["title"] ?: "Title",
            message.data["index"]?.toInt()!!
        )
    }

    fun sendNotification(body: String, title: String, index: Int) {

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationChannel = NotificationChannel(channel, description, IMPORTANCE_HIGH)
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = R.color.teal_700
        notificationChannel.enableVibration(true)
        notificationManager.createNotificationChannel(notificationChannel)

        intent = Intent(this, MainActivity::class.java)
        intent.putExtra("INDEX", index)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(this, 0, intent, FLAG_MUTABLE)
        } else {
            PendingIntent.getActivity(this, 0, intent, FLAG_ONE_SHOT)
        }


        builder = Notification.Builder(this, channel)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentText(body)
            .setContentTitle(title)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setColor(ContextCompat.getColor(this, R.color.teal_200))
            .setColorized(true)


        notificationManager.notify(1234, builder.build())
    }
}