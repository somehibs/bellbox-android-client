package de.circuitco.bellbox

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import de.circuitco.bellbox.bellbox.Api
import de.circuitco.bellbox.bellbox.ApiManager
import de.circuitco.bellbox.listfrags.BellFragment
import de.circuitco.bellbox.listfrags.BellringerFragment
import de.circuitco.bellbox.listfrags.PushCategoryFragment
import de.circuitco.bellbox.login.LoginActivity
import de.circuitco.bellbox.login.PushCheckCallback
import de.circuitco.bellbox.model.Push
import de.circuitco.bellbox.service.PushService

class MainActivity : AppCompatActivity() {
    private val logTag = "MainActivity"

    private var all = List(0) {Push()}

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStackImmediate()
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (!ApiManager.instance.isLoggedIn()) {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        Api.instance.MapBells(ApiManager.instance.token, PushCheckCallback(PushService.getToken(this)))
        supportFragmentManager.beginTransaction().add(R.id.container, PushCategoryFragment()).commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.get_push_id ->
                Toast.makeText(this, "PUSHID: " + PushService.getToken(this), Toast.LENGTH_LONG).show()
            R.id.show_bells ->
                // Set the primary content view
                setFrag(BellFragment())
            R.id.show_ringers ->
                // Set the primary content view
                setFrag(BellringerFragment())

        }
        return super.onOptionsItemSelected(item)
    }

    fun setFrag(frag : android.support.v4.app.Fragment) {
        supportFragmentManager.beginTransaction().add(R.id.container, frag).addToBackStack("").commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()

        if (!ApiManager.instance.isLoggedIn()) {
            startActivity(Intent(this, LoginActivity::class.java))
        }

//        all = getDatabase(this).pushDao().all
//
//        all.forEach {
//            Log.e(logTag, "Push received: $it")
//        }
//
//        //val info = findViewById<TextView>(R.id.infoText)
//        //info.text = ""
//        var infoText = ""
//
//        all.forEach {
//            infoText += it.title + "\n" + it.originalData + "\n\n"
//        }
//
//        //info.text = infoText
////        info.text = "${PushService.getToken(this)} $infoText"
//
//        val LOG_TAG = "token"
//        Log.e(LOG_TAG,PushService.getToken(this))
    }
}
