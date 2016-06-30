package joebruckner.lastpick.network

import joebruckner.lastpick.data.Movie

class HistoryManager() {
    private val history = mutableSetOf<Movie>()

    fun getHistory() = history

    fun addMovieToHistory(movie: Movie) = history.add(movie)
}