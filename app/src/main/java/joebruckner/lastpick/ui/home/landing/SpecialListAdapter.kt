package joebruckner.lastpick.ui.home.landing

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import joebruckner.lastpick.R
import joebruckner.lastpick.model.ListType
import joebruckner.lastpick.model.tmdb.CondensedMovie
import joebruckner.lastpick.utils.darkenColor
import joebruckner.lastpick.utils.find
import joebruckner.lastpick.utils.inflate
import joebruckner.lastpick.utils.loadWithPalette

class SpecialListAdapter(
        val context: Context,
        val showcasedMovies: Array<CondensedMovie>,
        val showcaseListener: (CondensedMovie) -> Unit,
        val seeAllListener: (ListType) -> Unit
): RecyclerView.Adapter<SpecialListAdapter.SpecialListViewHolder>() {

    val buttonTexts: Array<String>

    init {
        buttonTexts = context.resources.getStringArray(R.array.see_all_button_values)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecialListViewHolder {
        return SpecialListViewHolder(parent.inflate(R.layout.card_special_list))
    }

    override fun onBindViewHolder(holder: SpecialListViewHolder, position: Int) {
        val movie = showcasedMovies[position]
        holder.showcaseBackdrop.loadWithPalette(context, movie.getFullBackdropPath(), 0, 1f) {
            holder.showcaseTitle.setBackgroundColor(it.getPrimaryColor().darkenColor())
        }
        holder.showcaseTitle.text = movie.title
        holder.seeAllButton.text = buttonTexts[position]
        holder.showcase.setOnClickListener { showcaseListener.invoke(movie) }
        holder.seeAllButton.setOnClickListener {
            when (position) {
                0 -> seeAllListener.invoke(ListType.POPULAR)
                1 -> seeAllListener.invoke(ListType.NOW_PLAYING)
                2 -> seeAllListener.invoke(ListType.UPCOMING)
                else -> seeAllListener.invoke(ListType.TOP_RATED)
            }
        }

    }

    override fun getItemCount(): Int = 4

    class SpecialListViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val showcaseBackdrop by lazy { view.find<ImageView>(R.id.showcase_backdrop) }
        val showcaseTitle by lazy { view.find<TextView>(R.id.showcase_title) }
        val seeAllButton by lazy { view.find<Button>(R.id.see_all_button) }
        val showcase by lazy { view.find<View>(R.id.showcase) }
    }
}