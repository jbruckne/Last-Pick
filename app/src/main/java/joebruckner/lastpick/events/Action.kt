package joebruckner.lastpick.events

data class Action(
        val name: String
) {

    companion object {
        const val UPDATE = "shuffle"
    }
}