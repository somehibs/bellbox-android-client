package de.circuitco.bellbox.listfrags

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
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

@SuppressLint("SetTextI18n")
class BellringerFragment : Fragment(), ApiArrayResponse {
    override fun onFail() {
        errorText.text = "Failed to fetch bells from server"
    }

    override fun onResponse(obj: JSONArray) {
        list.adapter = BellringerListAdapter(obj)
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

class BellringerListAdapter(val json: JSONArray) : RecyclerView.Adapter<BellringerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BellringerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.bell_item, parent, false)
        return BellringerViewHolder(view)
    }

    override fun getItemCount(): Int {
        return json.length()
    }

    override fun onBindViewHolder(holder: BellringerViewHolder, position: Int) {
        val thisObject = json.getJSONObject(position)
        holder.name?.text = thisObject.getString("Name")
        holder.type?.text = "${thisObject.getString("Target")} ${thisObject.getString("RequestState")}"
        holder.key?.text = thisObject.getString("Urgent")
        holder.view.setOnLongClickListener {
            Toast.makeText(holder.view.context, "Name ${holder.name?.text} ${holder.key?.text}", Toast.LENGTH_LONG).show()
            true
        }
    }
}

data class BellringerViewHolder(var view: View,
                     var name: TextView? = view.findViewById(R.id.name),
                     var type: TextView? = view.findViewById(R.id.type),
                     var key: TextView? = view.findViewById(R.id.key)
                    ) : RecyclerView.ViewHolder(view)
