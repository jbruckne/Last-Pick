package joebruckner.lastpick.network

import com.squareup.otto.Bus
import com.squareup.otto.Subscribe
import joebruckner.lastpick.events.*
import java.util.*

class MovieManager(val bus: Bus, val scope: Int) {
    val idList = arrayListOf<Int>()

    init {
        bus.register(this)
    }

    @Subscribe public fun newMovieEvent(event: MovieEvent) {
        if (idList.isEmpty())
            bus.post(PageRequest(randomPage(1..scope)))
        else
            bus.post(MovieRequest(idList.removeAt(0)))
    }

    @Subscribe fun pageReponse(response: PageResponse) {
        idList.addAll(response.set.getIds())
        bus.post(MovieRequest(idList.removeAt(0)))
    }

    @Subscribe fun movieResponse(response: MovieResponse) {
        bus.post(response.movie)
    }

    private fun randomPage(range: Range<Int>): Int {
        val random = Random(System.currentTimeMillis())
        return random.nextInt(range.end - range.start) + range.start
    }
}
