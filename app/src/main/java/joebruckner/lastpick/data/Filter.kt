package joebruckner.lastpick.data

data class Filter(
        val showAll: Boolean = true,
        val genres: List<Genre> = emptyList(),
        val yearGte: String = DEFAULT_GTE,
        val yearLte: String = DEFAULT_LTE
) {
    fun allGenresSelected() = genres.equals(Genre.getAll())

    companion object {
        val DEFAULT_GTE = "1920"
        val DEFAULT_LTE = "2020"
    }
}