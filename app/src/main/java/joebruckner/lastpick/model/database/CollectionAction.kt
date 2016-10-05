package joebruckner.lastpick.model.database

data class CollectionAction(
        val action: Int,
        val id: Int,
        val name: String,
        val timestamp: Long = System.currentTimeMillis()
)