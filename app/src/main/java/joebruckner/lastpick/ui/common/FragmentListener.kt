package joebruckner.lastpick.ui.common

interface FragmentListener {
    fun onAttach()
    fun onCreateView()
    fun onViewCreated()
    fun onStart()
    fun onResume()
    fun onPause()
    fun onDestory()
}