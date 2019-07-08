package de.circuitco.bellbox.bellbox

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONObject

class ApiManager() {
	private lateinit var preferences: SharedPreferences
	val API_TOKEN = "api_token"
	var token = ""
		set(value) {
			field = value
			preferences.edit().putString(API_TOKEN, value).apply()
		}

	fun init(context : Context) {
		Api.init(context)
		preferences = context.getSharedPreferences("", 0)
		token = preferences.getString(API_TOKEN, "")
	}

	fun isLoggedIn(): Boolean {
		return token != ""
	}

	fun invalidateToken() {
		token = ""
		//loginInvalidatedCallback()
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

	companion object {
		lateinit var instance: ApiManager
		fun init(context: Context) {
			instance = ApiManager()
			instance.init(context)
		}
	}
}

interface LoginCallback {
	fun onLoggedIn()
	fun onError()
}
