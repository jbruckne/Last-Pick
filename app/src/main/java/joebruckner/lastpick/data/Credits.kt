package joebruckner.lastpick.data

data class Credits(
        val cast: List<Cast>,
        val crew: List<Crew>
) {
    override fun toString(): String {
        val str = StringBuilder()
        val size = if (cast.size > 3) 3 else cast.size
        for (i in 0..size-1) str.append(", ${cast[i].name}")
        return if (cast.size > 0) str.toString().substring(2) else str.toString()
    }
}