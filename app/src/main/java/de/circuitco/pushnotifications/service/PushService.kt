package de.circuitco.pushnotifications.service;

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage
import de.circuitco.pushnotifications.R
import de.circuitco.pushnotifications.model.AppDatabase
import de.circuitco.pushnotifications.model.Push

/**
 * Created by alex on 1/20/2018.
 */

class PushService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        var push = Push()
        var kv = ""
        remoteMessage.data.forEach({
            if (kv.isNotEmpty()) {
                kv += ","
            }
            kv += "\""+it.key+"\": \""+it.value+"\""
        })
        kv = """{$kv}"""
        Log.e("Test", kv);
        push.originalData = kv
        push.description = remoteMessage.data["message"]
        AppDatabase.getDatabase(this).pushDao().insert(push)
        notify(remoteMessage.data)
    }

    private fun notify(data: Map<String, String>) {
        if (data.contains("title") || data.contains("body")) {
            val manager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager? ?: return
            val src = data["source"] ?: "unknown"
            val e = when (src) {
                "irc" -> (Math.random()*1000).toInt()
                "vrc" -> 100
                "unknown" -> 100000
                else -> 100000
            }
            val builder: Notification.Builder
            builder = if (Build.VERSION.SDK_INT >= 26) {
                checkChannel(manager, src)
                Notification.Builder(this, src)
            } else {
                Notification.Builder(this)
            }

            builder.setLights(255, 500, 500)
            builder.setContentTitle(data["title"])
            builder.setContentText(data["body"])
            builder.setSmallIcon(R.drawable.irc)

            manager.notify(e, builder.build())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkChannel(manager: NotificationManager, channel: String) {
        val chan = NotificationChannel(channel, channel, NotificationManager.IMPORTANCE_HIGH)
        manager.createNotificationChannel(chan)
    }


//    /**
//     * Creates an IntentService.  Invoked by your subclass's constructor.
//     *
//     * @param name Used to name the worker thread, important only for debugging.
//     */
//    public PushService(String name) {
//        super("ZncPushService");
//    }

//    @Override
//    protected void onHandleIntent(@Nullable Intent intent) {
//
//    }
}
