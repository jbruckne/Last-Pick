package joebruckner.lastpick.view.common

interface FragmentListener {
    fun onCreate()
    fun onAttach()
    fun onCreateView()
    fun onViewCreated()
    fun onStart()
    fun onResume()
    fun onPause()
    fun onDestroy()
}