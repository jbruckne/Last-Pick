package joebruckner.lastpick.data

data class Filter(
        val genres: List<Genre> = Genre.getAll(),
        val yearGte: String = DEFAULT_GTE,
        val yearLte: String = DEFAULT_LTE
) {
    fun allGenresSelected() = genres.equals(Genre.getAll())

    companion object {
        val DEFAULT_GTE = "1920"
        val DEFAULT_LTE = "2020"
    }
}