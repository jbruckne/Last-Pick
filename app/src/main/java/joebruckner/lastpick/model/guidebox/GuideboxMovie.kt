package joebruckner.lastpick.model.guidebox

import com.google.gson.annotations.SerializedName

data class GuideboxMovie(
    val id: Int,
    val rating: String,
    val themoviedb: Int,
    @SerializedName("rottentomatoes")
    val rottenTomatoes: Int?,
    val imdb: String,
    val freebase: String,
    val metacritic: String?,
    @SerializedName("in_theaters")
    val inTheaters: Boolean,
    @SerializedName("free_web_sources")
    val freeWebSources: List<Source>,
    @SerializedName("subscription_web_sources")
    val subscriptionWebSources: List<Source>,
    @SerializedName("purchase_web_sources")
    val purchaseWebSources: List<Source>,
    @SerializedName("other_sources")
    val otherSources: Source.OtherSourcesWrapper?
)