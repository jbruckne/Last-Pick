package joebruckner.lastpick.view.common

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import joebruckner.lastpick.R
import joebruckner.lastpick.utilities.find

abstract class BaseFragment : Fragment(), BackPressListener {
    open val menuId: Int = R.menu.menu_empty
    abstract val layoutId: Int
    lateinit var parent: BaseActivity
    protected var isFirstStart = true
    val logTag: String = javaClass.simpleName

    val viewRoot by lazy { activity.find<ViewGroup>(android.R.id.content) }

    val listeners = mutableListOf<FragmentListener>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        //listeners.add(FragmentLifecycleLogger(logTag))

        listeners.forEach { it.onCreate() }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        parent = context as BaseActivity
        parent.inject(this)

        listeners.forEach { it.onAttach() }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        listeners.forEach { it.onCreateView() }
        return inflater.inflate(layoutId, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listeners.forEach { it.onViewCreated() }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(menuId, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onStart() {
        super.onStart()
        listeners.forEach { it.onStart() }
    }

    override fun onResume() {
        super.onResume()
        isFirstStart = false
        listeners.forEach { it.onResume() }
    }

    override fun onPause() {
        super.onPause()
        listeners.forEach { it.onPause() }
    }

    override fun onDestroy() {
        super.onDestroy()
        listeners.forEach { it.onDestroy() }
    }

    override fun onBackPressed(): Boolean {
        childFragmentManager.fragments?.forEach {
            if (it is BackPressListener && it.onBackPressed()) return true
        }
        return false
    }
}