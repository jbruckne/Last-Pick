package joebruckner.lastpick.ui.common

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import joebruckner.lastpick.R
import joebruckner.lastpick.utils.find

abstract class BaseFragment : Fragment(), BackPressListener {
    open val menuId: Int = R.menu.menu_empty
    abstract val layoutId: Int
    lateinit var parent: BaseActivity
    protected var isFirstStart = true
    val logTag: String = javaClass.simpleName

    val viewRoot by lazy { activity.find<ViewGroup>(android.R.id.content) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        parent = context as BaseActivity
        parent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(layoutId, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(menuId, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onResume() {
        super.onResume()
        isFirstStart = false
    }

    override fun onBackPressed(): Boolean {
        childFragmentManager.fragments?.forEach {
            if (it is BackPressListener && it.onBackPressed()) return true
        }
        return false
    }
}