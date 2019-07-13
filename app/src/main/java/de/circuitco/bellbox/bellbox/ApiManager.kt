package de.circuitco.bellbox.bellbox

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import com.android.volley.VolleyError
import de.circuitco.bellbox.login.LoginActivity
import org.json.JSONObject

class ApiManager {
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

	fun handleError(it: VolleyError?) {
		val status = it?.networkResponse?.statusCode
		if (status == 403) {
			// Failed authentication
			token = ""
		} else {
			val body = it?.networkResponse?.data
			Log.w("Api", "Request failed: " + status)
			try { Log.w("Api", "body: " + String(body!!)) } catch (e: KotlinNullPointerException) {

			}
		}
		if (!isLoggedIn()) {
			de.circuitco.bellbox.Application.instance.startActivity(Intent(de.circuitco.bellbox.Application.instance, LoginActivity::class.java))
		}
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
