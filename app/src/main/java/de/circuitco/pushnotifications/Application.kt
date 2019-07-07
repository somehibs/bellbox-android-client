package de.circuitco.pushnotifications

import android.app.Application
import de.circuitco.pushnotifications.bellbox.ApiManager

class Application : Application() {
    companion object {
        lateinit var instance: de.circuitco.pushnotifications.Application
    }

    override fun onCreate() {
        super.onCreate()

        instance = this

        ApiManager.init(this)
    }
}
