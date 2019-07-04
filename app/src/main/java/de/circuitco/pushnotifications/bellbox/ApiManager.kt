package de.circuitco.pushnotifications.bellbox

import android.content.Context

class ApiManager : () -> Unit {
    override fun invoke() {
        // Login invalidated, tell someone else?
    }

    lateinit var loginManager : LoginManager
    fun Init(ctx : Context) {
        Api.init(ctx)
        loginManager = LoginManager(this)
        loginManager.Init(ctx.getSharedPreferences("", 0))
    }
}