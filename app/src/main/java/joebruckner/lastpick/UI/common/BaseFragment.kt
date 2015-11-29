package joebruckner.lastpick.ui.common

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import joebruckner.lastpick.events.Action
import joebruckner.lastpick.ui.common.BaseActivity

abstract class BaseFragment : Fragment() {
    abstract val layoutId: Int
    lateinit var parent: BaseActivity

    val logTag = javaClass.simpleName

    override fun onAttach(context: Context) {
        super.onAttach(context)
        parent = context as BaseActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(layoutId, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState == null) Log.d(logTag, "Initial Creation!!!!")
        else Log.d(logTag, "Returning")
    }

    override fun onResume() {
        super.onResume()
        Log.d(logTag, "Resuming!!!")
    }

    open fun handleAction(action: Action): Unit {}


}