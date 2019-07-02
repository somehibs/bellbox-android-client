package de.circuitco.pushnotifications.service

import android.content.Context
import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService

/**
 * Created by alex on 1/20/2018.
 */
class FirebaseInstance : FirebaseInstanceIdService() {
    val logTag = "FirebaseInstance"

    override fun onTokenRefresh()
    {
        val refreshedToken = FirebaseInstanceId.getInstance().token ?: ""
        Log.e(logTag, "Token refresh: " + refreshedToken)
        saveToken(this, refreshedToken)
    }

    companion object Token
    {
        fun saveToken(ctx: Context, token: String): String {
            return token
        }

        fun getToken(ctx: Context): String? {
            return FirebaseInstanceId.getInstance().token ?: ""
        }
    }
}
