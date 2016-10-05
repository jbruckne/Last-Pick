package joebruckner.lastpick.model.presentation

import joebruckner.lastpick.model.tmdb.Genre

data class Filter(
        val showAll: Boolean = true,
        val genres: List<Genre> = emptyList(),
        val yearGte: String = DEFAULT_GTE,
        val yearLte: String = DEFAULT_LTE
) {
    fun allGenresSelected() = genres.equals(Genre.getAll())

    fun genresToBooleanArray(): BooleanArray {
        return Genre.getAll()
                .map { genres.contains(it) }
                .toBooleanArray()
    }

    fun genresToString(): String {
        val sb = StringBuilder()
        genres.forEach {
            sb.append(it.name.substring(0..1))
        }
        return sb.toString()
    }

    companion object {
        val DEFAULT_GTE = "1920"
        val DEFAULT_LTE = "2020"
    }
}