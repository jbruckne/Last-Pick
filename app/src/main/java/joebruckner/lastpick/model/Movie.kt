package joebruckner.lastpick.model

import joebruckner.lastpick.model.guidebox.GuideboxMovie
import joebruckner.lastpick.model.guidebox.Source
import joebruckner.lastpick.model.tmdb.*
import joebruckner.lastpick.model.tmdb.Collection

data class Movie(
        val id: Int,
        val title: String,
        val overview: String,
        val tagline: String,
        val rating: String?,
        val releaseDate: String,
        val runtime: Int,
        val genres: List<Genre>,
        val belongsToCollection: Collection?,
        val credits: Credits,
        val adult: Boolean,
        val posterPath: String?,
        val backdropPath: String?,
        val budget: Int,
        val revenue: Long,
        val status: String,
        val voteAverage: Double,
        val videos: List<Video>,
        val backdrops: List<Image>,
        val posters: List<Image>,
        val homepage: String,
        val imdbId: String,
        val freebaseId: String?,
        val rottenTomatoesId: Int?,
        val metacriticLink: String?,
        val sources: List<Source>
) {

    fun getFullPosterPath(): String = imageUrl + POSTER_SIZE + posterPath

    fun getFullBackdropPath(): String = imageUrl + BACKDROP_SIZE + backdropPath

    companion object {
        val rottenTomatoesUrl="https://www.rottentomatoes.com/m/"
        val theMovieDatabaseUrl = "http://www.themoviedb.com/movie/"
        val youtubeUrl = "https://www.youtube.com/watch?v="
        val imageUrl = "http://image.tmdb.org/t/p/"
        val BACKDROP_SIZE = "w1280"
        val POSTER_SIZE = "w342"

        fun createMovie(tmdb: TmdbMovie, guidebox: GuideboxMovie?): Movie {
            val sources = guidebox?.let {
                it.freeWebSources
                        .plus(it.subscriptionWebSources)
                        .plus(it.purchaseWebSources)
                        .plus(it.otherSources?.movieTheater ?: emptyList())
            } ?: emptyList()
            return Movie(
                    tmdb.id,
                    tmdb.title,
                    tmdb.overview,
                    tmdb.tagline,
                    guidebox?.rating,
                    tmdb.releaseDate,
                    tmdb.runtime,
                    tmdb.genres,
                    tmdb.belongsToCollection,
                    tmdb.credits,
                    tmdb.adult,
                    tmdb.posterPath,
                    tmdb.backdropPath,
                    tmdb.budget,
                    tmdb.revenue,
                    tmdb.status,
                    tmdb.voteAverage,
                    tmdb.videos.results,
                    tmdb.images.backdrops,
                    tmdb.images.posters,
                    tmdb.homepage,
                    tmdb.imdbId,
                    guidebox?.freebase,
                    guidebox?.rottenTomatoes,
                    guidebox?.metacritic,
                    sources
            )
        }
    }
}