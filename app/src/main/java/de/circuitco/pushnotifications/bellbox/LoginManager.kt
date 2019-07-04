package de.circuitco.pushnotifications.bellbox

import android.content.SharedPreferences
import org.json.JSONObject

class LoginManager(private var loginInvalidatedCallback: () -> Unit) {
	private lateinit var preferences: SharedPreferences
	val API_TOKEN = "api_token"
	var token = ""

	fun Init(prefs : SharedPreferences) {
		preferences = prefs
		token = prefs.getString(API_TOKEN, "")
	}

	fun IsLoggedIn(): Boolean {
		return token != ""
	}

	fun InvalidateToken() {
		token = ""
		loginInvalidatedCallback()
	}

    fun Login(user: String, pass: String, loginCallback :LoginCallback) {
		Api.instance.Login(user, pass, object : ApiResponse {
			override fun onResponse(obj: JSONObject) {
				token = obj.getString("token")
				if (token == "") {
					loginCallback.onError()
					return
				} else {
					loginCallback.onLoggedIn()
				}
			}

			override fun onFail() {
				loginCallback.onError()
			}
		})
	}
}

interface LoginCallback {
	fun onLoggedIn()
	fun onError()
}