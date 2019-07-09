package de.circuitco.bellbox

import android.support.v4.app.Fragment

open abstract class BellboxFragment : Fragment() {

    override fun onResume() {
        super.onResume()
        refresh()
    }

    open fun refresh() {
        (activity as MainActivity).setBarTitle(getBarTitle())
    }

    abstract fun getBarTitle(): String
}
