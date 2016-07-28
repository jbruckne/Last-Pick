package joebruckner.lastpick.data

data class Video(
        val id: String,
        val key: String,
        val name: String,
        val site: String,
        val size: Int,
        val type: String
) {

    data class ListWrapper(val results: List<Video> = arrayListOf())

    fun getTrailerUrl() = "https://www.youtube.com/watch?v=$key"

    fun getThumbnailUrl() = "https://img.youtube.com/vi/$key/0.jpg"
}