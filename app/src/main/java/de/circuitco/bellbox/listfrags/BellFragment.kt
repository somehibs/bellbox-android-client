package de.circuitco.bellbox.listfrags

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
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
import de.circuitco.bellbox.BellboxFragment
import de.circuitco.bellbox.MainActivity
import de.circuitco.bellbox.R
import de.circuitco.bellbox.bellbox.Api
import de.circuitco.bellbox.bellbox.ApiArrayResponse
import de.circuitco.bellbox.bellbox.ApiManager
import de.circuitco.bellbox.bellbox.ApiResponse
import kotlinx.android.synthetic.main.bells.*
import org.json.JSONArray
import org.json.JSONObject

@SuppressLint("SetTextI18n")
class BellFragment : BellboxFragment(), ApiArrayResponse {
    override fun getBarTitle(): String = "Bells"

    override fun onFail() {
        errorText.text = "Failed to fetch bells from server"
    }

    override fun onResponse(obj: JSONArray) {
        list.adapter = BellListAdapter(obj) {
            Toast.makeText(context, "Name ${it.getString("Name")} ${it.getString("Key")}", Toast.LENGTH_LONG).show()
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

    override fun refresh() {
        super.refresh()

        Api.instance.MapBells(ApiManager.instance.token, this)
    }
}

class BellListAdapter(val json: JSONArray, val onClick: (JSONObject) -> Unit) : RecyclerView.Adapter<BellViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BellViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.bell_item, parent, false)
        return BellViewHolder(view)
    }

    override fun getItemCount(): Int {
        return json.length()
    }

    override fun onBindViewHolder(holder: BellViewHolder, position: Int) {
        val thisObject = json.getJSONObject(position)
        holder.bind(thisObject)
        holder.name?.text = thisObject.getString("Name")
        holder.type?.text = thisObject.getString("Type")
        holder.key?.text = thisObject.getString("Key")
    }
}

data class BellViewHolder(var view: View,
                     var name: TextView? = view.findViewById(R.id.name),
                     var type: TextView? = view.findViewById(R.id.type),
                     var key: TextView? = view.findViewById(R.id.key)
                    ) : RecyclerView.ViewHolder(view), View.OnClickListener {
    var obj = JSONObject()
    fun delete() {
        val name = obj.getString("Name")
        //val type = obj.getString("Type")
        val key = obj.getString("Key")
        Api.instance.DeleteBell(ApiManager.instance.token, key, name, object : ApiResponse {
            override fun onFail() {

            }

            override fun onResponse(obj: JSONObject) {
                // Deleted
                Toast.makeText(view.context, "Deleted bell ${name} from the server!", Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onClick(v: View?) {
        AlertDialog.Builder(view.context).setTitle("Manage ${name?.text.toString()}").setMessage("What do you want to do?").setPositiveButton("Delete") { _: DialogInterface, _: Int -> delete() }.create().show()
    }

    fun bind(item: JSONObject) {
        obj = item
        view.setOnClickListener(this)
    }
}
