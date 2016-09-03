package joebruckner.lastpick.ui.common

import android.util.Log

class FragmentLifecycleLogger(val logTag: String): FragmentListener {

    override fun onCreate() {
        Log.d(logTag, "onCreate")
    }

    override fun onAttach() {
        Log.d(logTag, "onAttach")
    }

    override fun onCreateView() {
        Log.d(logTag, "onCreateView")
    }

    override fun onViewCreated() {
        Log.d(logTag, "onViewCreated")
    }

    override fun onStart() {
        Log.d(logTag, "onStart")
    }

    override fun onResume() {
        Log.d(logTag, "onResume")
    }

    override fun onPause() {
        Log.d(logTag, "onPause")
    }

    override fun onDestroy() {
        Log.d(logTag, "onDestroy")
    }
}