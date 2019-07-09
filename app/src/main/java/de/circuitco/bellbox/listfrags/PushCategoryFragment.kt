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
import kotlinx.android.synthetic.main.bells.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray

@SuppressLint("SetTextI18n")
class PushCategoryFragment : Fragment(), OnCategoryClick {
    override fun click(category: String) {
        (activity as MainActivity).setFrag(PushFragment.new(category))
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
        GlobalScope.launch {
            refreshFromDatabase()
        }
    }

    private fun refreshFromDatabase() {
        val senderMap = HashMap<String, Long>()
        val senders = AppDatabase.getDatabase(context).pushDao().findSenders()
        for (sender in senders) {
            val count = AppDatabase.getDatabase(context).pushDao().countBySender(sender)
            senderMap[sender] = count
        }
        val adapter = PushCategoryListAdapter(senders, senderMap, this)
        activity?.runOnUiThread {
            list.adapter = adapter
        }
    }
}

interface OnCategoryClick {
    fun click(category: String)
}

class PushCategoryListAdapter(val senders: List<String>, val map: Map<String, Long>, val click :OnCategoryClick) : RecyclerView.Adapter<PushCategoryViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PushCategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.bell_item, parent, false)
        return PushCategoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return senders.size
    }

    override fun onBindViewHolder(holder: PushCategoryViewHolder, position: Int) {
        val thisName = senders[position]
        val thisObject = map[thisName]
        holder.name?.text = thisName
        holder.type?.text = "${thisObject.toString()} push notifications"
        holder.view.setOnLongClickListener {
            Toast.makeText(holder.view.context, "Name ${holder.name?.text} ${holder.key?.text}", Toast.LENGTH_LONG).show()
            true
        }
        holder.view.setOnClickListener {
            click.click(thisName)
        }
    }
}

data class PushCategoryViewHolder(var view: View,
                                  var name: TextView? = view.findViewById(R.id.name),
                                  var type: TextView? = view.findViewById(R.id.type),
                                  var key: TextView? = view.findViewById(R.id.key)
                    ) : RecyclerView.ViewHolder(view)
