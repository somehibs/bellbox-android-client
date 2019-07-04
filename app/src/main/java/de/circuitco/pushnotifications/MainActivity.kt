package de.circuitco.pushnotifications

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.iid.FirebaseInstanceId
import de.circuitco.pushnotifications.model.AppDatabase.getDatabase
import de.circuitco.pushnotifications.model.Push
import de.circuitco.pushnotifications.service.PushService

class MainActivity : AppCompatActivity() {
    private val logTag = "MainActivity"

    private var all = List(0, {Push()})

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if ( item.itemId == R.id.get_push_id ) {
            Toast.makeText(this, "PUSHID: " + PushService.getToken(this), Toast.LENGTH_LONG).show()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()

        all = getDatabase(this).pushDao().all
        
        all.forEach {
            Log.e(logTag, "Push received: $it")
        }

        val info = findViewById<TextView>(R.id.infoText)
        info.text = ""
        var infoText = ""

        all.forEach {
            infoText += it.title + "\n" + it.originalData + "\n\n"
        }

        //info.text = infoText
        info.text = "${PushService.getToken(this)} $infoText"

        val LOG_TAG = "wat"
        Log.e(LOG_TAG,FirebaseInstanceId.getInstance().token ?: "no token")
    }


}
