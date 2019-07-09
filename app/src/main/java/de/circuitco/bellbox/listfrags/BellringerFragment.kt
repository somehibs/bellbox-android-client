package de.circuitco.bellbox.listfrags

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import de.circuitco.bellbox.R
import de.circuitco.bellbox.bellbox.Api
import de.circuitco.bellbox.bellbox.ApiArrayResponse
import de.circuitco.bellbox.bellbox.ApiManager
import kotlinx.android.synthetic.main.bells.*
import org.json.JSONArray
import org.json.JSONObject

@SuppressLint("SetTextI18n")
class BellringerFragment : Fragment(), ApiArrayResponse {
    override fun onFail() {
        errorText.text = "Failed to fetch bells from server"
    }

    override fun onResponse(obj: JSONArray) {
        list.adapter = BellringerListAdapter(obj) {
            val state = it.getInt("RequestState")
            if (state == 1) {
                // deny
            } else {
                // allow
            }
            //AlertDialog.Builder()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bells, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        list.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onResume() {
        super.onResume()
        // Refresh from the server
        Api.instance.MapSenders(ApiManager.instance.token, this)
    }
}

class BellringerListAdapter(val json: JSONArray, val onClick: (JSONObject) -> Unit) : RecyclerView.Adapter<BellringerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BellringerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.bell_item, parent, false)
        return BellringerViewHolder(view, onClick)
    }

    override fun getItemCount(): Int {
        return json.length()
    }

    override fun onBindViewHolder(holder: BellringerViewHolder, position: Int) {
        val thisObject = json.getJSONObject(position)
        holder.bind(thisObject)
    }
}

data class BellringerViewHolder(var view: View,
                                  var clickParent: (JSONObject) -> Unit,
                                  var name: TextView? = view.findViewById(R.id.name),
                                  var type: TextView? = view.findViewById(R.id.type),
                                  var key: TextView? = view.findViewById(R.id.key)
) : RecyclerView.ViewHolder(view), View.OnClickListener {
    var ringer: JSONObject = JSONObject()

    override fun onClick(v: View?) {
        clickParent(ringer)
    }

    fun bind(ringer: JSONObject) {
        this.ringer = ringer
        val rqs = when (ringer.getString("RequestState")) {
            "0" -> "Pending"
            "1" -> "Allowed"
            "2" -> "Denied"
            else -> "Unknown state"
        }
        name?.text = ringer.getString("Name")
        type?.text = "${ringer.getString("Target")} $rqs"
        key?.text = ringer.getString("Urgent")
        view.setOnClickListener(this)
    }
}
