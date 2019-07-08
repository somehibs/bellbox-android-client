package de.circuitco.bellbox

import android.app.Application
import de.circuitco.bellbox.bellbox.ApiManager

class Application : Application() {
    companion object {
        lateinit var instance: de.circuitco.bellbox.Application
    }

    override fun onCreate() {
        super.onCreate()

        instance = this

        ApiManager.init(this)
    }
}
