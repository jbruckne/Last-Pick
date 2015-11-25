package joebruckner.lastpick.events

data class RequestError(
        val message: String,
        val code: Int
)