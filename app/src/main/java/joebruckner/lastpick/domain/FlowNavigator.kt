package joebruckner.lastpick.domain

import joebruckner.lastpick.model.ListType

interface FlowNavigator {
    fun sendFeedback()
    fun share(url: String, message: String? = null)
    fun view(url: String)
    fun showHome()
    fun showRandom()
    fun showMovie(id: Int)
    fun showSpecial(type: ListType)
    fun showAbout()
    fun showImage(url: String)
}