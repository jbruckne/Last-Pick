package joebruckner.lastpick.domain

import joebruckner.lastpick.model.presentation.Showcase

interface FlowNavigator {
    fun sendFeedback()
    fun share(url: String, message: String? = null)
    fun view(url: String)
    fun showHome()
    fun showRandom()
    fun showMovie(id: Int)
    fun showSpecial(type: Showcase)
    fun showAbout()
    fun showLogin()
    fun showLogout()
}