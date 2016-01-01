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

    @Subscribe public fun newMovieEvent(request: RandomMovieRequest) {
        if (idList.isEmpty())
            bus.post(PageRequest(randomPage(1..scope)))
        else
            bus.post(MovieRequest(idList.removeAt(0)))
    }

    @Subscribe fun pageReponse(event: PageEvent) {
        idList.addAll(event.page.getIds())
        bus.post(MovieRequest(idList.removeAt(0)))
    }

    private fun randomPage(range: Range<Int>): Int {
        val random = Random(System.currentTimeMillis())
        return random.nextInt(range.end - range.start) + range.start
    }
}
