package de.circuitco.bellbox.service;

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import de.circuitco.bellbox.MainActivity
import de.circuitco.bellbox.model.AppDatabase
import de.circuitco.bellbox.model.KnownSender
import de.circuitco.bellbox.model.Push
import java.lang.NullPointerException
import java.util.*
import android.support.v4.app.NotificationCompat
import de.circuitco.bellbox.R


/**
 * Created by alex on 1/20/2018.
 */

class PushService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        var push = Push()
        var kv = ""
        remoteMessage.data.forEach {
            if (kv.isNotEmpty()) {
                kv += ","
            }
            kv += "\""+it.key+"\": \""+it.value+"\""
        }
        kv = """{$kv}"""
        Log.e("Test", kv);
        push.originalData = kv
        push.description = remoteMessage.data["body"]
        push.timestamp = remoteMessage.data["time"]
        push.title = remoteMessage.data["title"]
        push.sender = remoteMessage.data["sender"]
        //push.url = remoteMessage.data["url"]
        AppDatabase.getDatabase(this).pushDao().insert(push)
        try {
            changed.postValue(changed.getValue()!!+1L)
        } catch (e :Exception) {
            Log.e("LOG", "Push notification received but crashed", e)
        }
        notify(remoteMessage.data)
    }

    private fun notify(data: Map<String, String>) {
        if (data.contains("title") || data.contains("body")) {
            val manager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager? ?: return
            val src = data["sender"] ?: "unknown"
            // Find out if this sender is in the database already
            val senderDao = AppDatabase.getDatabase(this).knownSenderDao()
            var sender = KnownSender()
            sender.sender = src
            var dbSender = senderDao.findBySender(src)
            if (dbSender.size == 0) {
                senderDao.insert(sender)
                dbSender = senderDao.findBySender(src)
            }
            if (dbSender.size > 0) {
                sender = dbSender[0]
            } else {
                throw NullPointerException("Oh fuck this isn't supposed to happen")
            }
            val builder: NotificationCompat.Builder
            builder = if (Build.VERSION.SDK_INT >= 26) {
                checkChannel(manager, ""+sender.uid, src)
                NotificationCompat.Builder(this, ""+sender.uid)
            } else {
                NotificationCompat.Builder(this)
            }
            val bigStyle = NotificationCompat.BigTextStyle()
            bigStyle.setBigContentTitle(data["title"])
            bigStyle.bigText(data["body"])

            builder.setLights(255, 500, 500)
            builder.setContentTitle(data["title"])
            builder.setContentText(data["body"])
            /*.setStyle(NotificationCompat.InboxStyle()
                .addLine(messageSnippet1)
                .addLine(messageSnippet2))*/
//            builder.setGroup(data["sender"])
//            builder.setGroupSummary(true)
            builder.setAutoCancel(true)
            builder.setSmallIcon(R.drawable.irc)
            if (sender.sender == "Maps location") {
                builder.setSmallIcon(R.drawable.running_woman_512)
            }
//            if (data["time"] == null) {
                builder.setWhen(Date().time)
//            } else {
//                builder.setWhen(data["time"]!!.toLong())
//            }
            builder.setStyle(bigStyle)
            builder.setShowWhen(true)
            //builder.setSmallIcon(R.drawable.irc)
            if (data.containsKey("url") && data["url"].isNullOrEmpty() == false) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(data["url"]))
                builder.setContentIntent(PendingIntent.getActivity(this, 0, intent, 0))
            } else {
                builder.setContentIntent(PendingIntent.getActivity(this, 0, Intent(this, MainActivity::class.java), 0))
            }

            manager.notify(10+sender.uid, builder.build())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkChannel(manager: NotificationManager, uid: String, channel: String) {
        val existingChannel = manager.getNotificationChannel(uid)
        if (existingChannel?.name != channel) {
            manager.deleteNotificationChannel(uid)
        }
        val chan = NotificationChannel(uid, channel, NotificationManager.IMPORTANCE_HIGH)

        manager.createNotificationChannel(chan)
    }

    override fun onNewToken(token: String) {
        // Report to server, remove previous token
        saveToken(token, this)
    }

    companion object {
        var changed = MutableLiveData<Long>()
        private var FCM_KEY = "fcmkey"
        fun getToken(context: Context): String {
            return context.getSharedPreferences("", 0).getString(FCM_KEY, "")
        }

        fun saveToken(token: String, context: Context) {
            val prefs = context.getSharedPreferences("", 0)
            prefs.edit().putString(FCM_KEY, token).apply()
        }
    }
}
