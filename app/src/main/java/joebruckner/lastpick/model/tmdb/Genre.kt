package joebruckner.lastpick.model.tmdb

data class Genre (
    val id: Int,
    val name: String
) {
    companion object {
        val ACTION          = Genre(28, "Action")
        val ADVENTURE       = Genre(12, "Adventure")
        val ANIMATION       = Genre(16, "Animation")
        val COMEDY          = Genre(35, "Comedy")
        val CRIME           = Genre(80, "Crime")
        val DOCUMENTARY     = Genre(99, "Documentary")
        val DRAMA           = Genre(18, "Drama")
        val FAMILY          = Genre(10751, "Family")
        val FANTASY         = Genre(14, "Fantasy")
        val FOREIGN         = Genre(10769, "Foreign")
        val HISTORY         = Genre(36, "History")
        val HORROR          = Genre(27, "Horror")
        val MUSIC           = Genre(10402, "Music")
        val MYSTERY         = Genre(9648, "Mystery")
        val ROMANCE         = Genre(10749, "Romance")
        val SCIENCE_FICTION = Genre(878, "Science Fiction")
        val THRILLER        = Genre(53, "Thriller")
        val WAR             = Genre(10752, "War")
        val WESTERN         = Genre(37, "Western")

        fun getAll(): List<Genre> {
            return listOf(ACTION, ADVENTURE, ANIMATION, COMEDY, CRIME,
                    DOCUMENTARY, DRAMA, FAMILY, FANTASY, FOREIGN, HISTORY, HORROR,
                    MUSIC, MYSTERY, ROMANCE, SCIENCE_FICTION, THRILLER, WAR, WESTERN)
        }

        fun getGenre(id: Int): Genre? {
            getAll().forEach { if (it.id == id) return it }
            return null
        }
    }
}