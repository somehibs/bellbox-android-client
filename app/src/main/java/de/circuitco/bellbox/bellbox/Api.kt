package de.circuitco.bellbox.bellbox

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class Api(val queue: RequestQueue) {
    val HOST = "circuitco.de"
    val PORT = 5384
    val AUTH_HEADER = "Authorization"

    fun Login(user: String, pass: String, responseCallback :ApiResponse) {
        val request = JSONObject()
        request.put("user", user)
        request.put("password", pass)
        PostObj("user/login", request, responseCallback)
    }

    private fun PostObj(path : String, request: JSONObject, responseCallback: ApiResponse) {
        PostObj(path, "", request, responseCallback)
    }

    private fun PostObj(path : String, token : String, request: JSONObject, responseCallback: ApiResponse) {
        val jsonObjectRequest = object : JsonObjectRequest(Request.Method.POST, getUrl() + path, request, Response.Listener {
            responseCallback.onResponse(it)
        }, Response.ErrorListener {
            ApiManager.instance.handleError(it)
            responseCallback.onFail()
        }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String,String>()
                if (token != "") {
                    headers[AUTH_HEADER] = token
                }
                return headers
            }
        }
        queue.add(jsonObjectRequest)
    }

    fun NewBell(token : String, name : String, key : String, responseCallback :ApiResponse) {
        val request = JSONObject()
        request.put("name", name)
        request.put("type", "ANDROID")
        request.put("key", key)
        PostObj("bell/new", token, request, responseCallback)
    }

    fun MapBells(token : String, responseCallback :ApiArrayResponse) {
        GetArray("bell/map", token, responseCallback)
    }

    fun MapSenders(token : String, responseCallback :ApiArrayResponse) {
        GetArray("send/map", token, responseCallback)
    }

    fun AcceptSender(token : String, name : String, responseCallback :ApiResponse) {
        val request = JSONObject()
        request.put("Name", name)
        PostObj("send/accept", token, request, responseCallback)
    }

    fun DeleteBell(token : String, bellKey: String, bellName: String, responseCallback :ApiResponse) {
        val request = JSONObject()
        request.put("key", bellKey)
        request.put("name", bellName)
        PostObj("bell/delete", token, request, responseCallback)
    }

    fun DenySender(token : String, name : String, responseCallback :ApiResponse) {
        val request = JSONObject()
        request.put("Name", name)
        PostObj("send/deny", token, request, responseCallback)
    }

    fun getUrl() :String {
        return "http://%s:%d/".format(HOST, PORT)
    }

    private fun GetArray(path: String, token: String, responseCallback: ApiArrayResponse) {
        val jsonObjectRequest = object : JsonArrayRequest(
                getUrl() + path,
                Response.Listener {
                    responseCallback.onResponse(it)
                },
                Response.ErrorListener {
                    ApiManager.instance.handleError(it)
                    responseCallback.onFail()
                }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String,String>()
                headers[AUTH_HEADER] = token
                return headers
            }
        }
        queue.add(jsonObjectRequest)
    }

    companion object {
        lateinit var instance : Api

        fun init(ctx : Context) {
            instance = Api(Volley.newRequestQueue(ctx))
        }
    }
}

interface ApiResponse {
    fun onResponse(obj :JSONObject)
    fun onFail()
}

interface ApiArrayResponse {
    fun onResponse(obj :JSONArray)
    fun onFail()
}
