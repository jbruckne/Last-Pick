package joebruckner.lastpick.ui

import android.content.Context
import android.support.v4.app.Fragment
import joebruckner.lastpick.events.Action

abstract class BaseFragment : Fragment() {

    lateinit var coordinator: Coordinator

    override fun onAttach(context: Context) {
        super.onAttach(context)
        coordinator = context as Coordinator
    }

    abstract fun handleAction(action: Action): Unit
}