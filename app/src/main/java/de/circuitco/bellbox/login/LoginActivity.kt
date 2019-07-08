package de.circuitco.bellbox.login

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import de.circuitco.bellbox.Application
import de.circuitco.bellbox.MainActivity
import de.circuitco.bellbox.R
import de.circuitco.bellbox.bellbox.*
import de.circuitco.bellbox.service.PushService
import kotlinx.android.synthetic.main.login.*
import org.json.JSONArray
import org.json.JSONObject

class PushCheckCallback(private val token: String) : ApiArrayResponse {
    override fun onResponse(obj: JSONArray) {
        for (i in 0 until obj.length()) {
            val item = obj.getJSONObject(i)
            if (item.getString("Key") == token) {
                Toast.makeText(Application.instance, "Found bell", Toast.LENGTH_LONG).show()
                return
            }
        }
        // not found
        Api.instance.NewBell(ApiManager.instance.token, "Android " + Build.MANUFACTURER + " " + Build.MODEL, token, object : ApiResponse{
            override fun onResponse(obj: JSONObject) {
                Toast.makeText(Application.instance, "Added OK", Toast.LENGTH_LONG).show()
            }

            override fun onFail() {
                Toast.makeText(Application.instance, "Failed to add", Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onFail() {
        Toast.makeText(Application.instance, "Failed to check bells", Toast.LENGTH_LONG).show()
    }

}

class LoginActivity : AppCompatActivity(), LoginCallback {
    override fun onLoggedIn() {
        // Success, take them back to the dashboard
        Toast.makeText(this, "Logged in!", Toast.LENGTH_LONG).show()
        Api.instance.MapBells(ApiManager.instance.token, PushCheckCallback(PushService.getToken(this)))
        startActivity(Intent(this, MainActivity::class.java))
    }

    override fun onError() {
        Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        login.setOnClickListener {
            if (user.text.isNotEmpty() && pass.text.isNotEmpty()) {
                ApiManager.instance.Login(user.text.toString(), pass.text.toString(), this)
            }
        }
    }
}
