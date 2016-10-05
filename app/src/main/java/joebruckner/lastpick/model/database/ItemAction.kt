package joebruckner.lastpick.model.database

data class ItemAction(
        val action: Int,
        val movieId: Int,
        val collectionId: Int,
        val timestamp: Long = System.currentTimeMillis()
)