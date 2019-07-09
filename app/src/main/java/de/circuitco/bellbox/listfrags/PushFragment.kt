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
import de.circuitco.bellbox.MainActivity
import de.circuitco.bellbox.R
import de.circuitco.bellbox.model.AppDatabase
import de.circuitco.bellbox.model.Push
import kotlinx.android.synthetic.main.bells.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray

@SuppressLint("SetTextI18n")
class PushFragment : Fragment() {
    var sender = ""

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
        sender = arguments?.getString("SENDER") ?: ""
        GlobalScope.launch {
            refreshFromDatabase()
        }
    }

    suspend fun refreshFromDatabase() {
        val notifications = AppDatabase.getDatabase(context).pushDao().findBySender(sender)
        val adapter = PushAdapter(notifications)
        activity?.runOnUiThread {
            list.adapter = adapter
        }
    }

    companion object {
        fun new(name: String): PushFragment {
            val frag = PushFragment()
            val args = Bundle()
            args.putString("SENDER", name)
            frag.arguments = args
            return frag
        }
    }
}

class PushAdapter(val senders: List<Push>) : RecyclerView.Adapter<PushViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PushViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.bell_item, parent, false)
        return PushViewHolder(view)
    }

    override fun getItemCount(): Int {
        return senders.size
    }

    override fun onBindViewHolder(holder: PushViewHolder, position: Int) {
        val push = senders[position]
        holder.bind(push)
    }
}

data class PushViewHolder(var view: View,
                                  var name: TextView? = view.findViewById(R.id.name),
                                  var type: TextView? = view.findViewById(R.id.type),
                                  var key: TextView? = view.findViewById(R.id.key)
                    ) : RecyclerView.ViewHolder(view) {
    fun bind(push: Push) {
        name?.text = push.title
        type?.text = push.description
    }
}
