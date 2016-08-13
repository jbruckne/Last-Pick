package joebruckner.lastpick.ui.home.landing

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import joebruckner.lastpick.R
import joebruckner.lastpick.model.Showcase
import joebruckner.lastpick.model.tmdb.SlimMovie
import joebruckner.lastpick.utils.find
import joebruckner.lastpick.utils.inflate
import joebruckner.lastpick.utils.load

class ShowcaseAdapter(
        val context: Context,
        val showcaseListener: (SlimMovie) -> Unit,
        val viewMoreListener: (Showcase) -> Unit
): RecyclerView.Adapter<ShowcaseAdapter.SpecialListViewHolder>() {
    private val showcases = Showcase.values().toList()
    private val movies = mutableMapOf<Showcase, SlimMovie>()

    fun addPair(movie: SlimMovie, type: Showcase) {
        movies.put(type, movie)
        notifyItemChanged(showcases.indexOf(type))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecialListViewHolder {
        return SpecialListViewHolder(parent.inflate(R.layout.card_showcase))
    }

    override fun onBindViewHolder(holder: SpecialListViewHolder, position: Int) {
        val type = showcases[position]
        val movie = movies[type]
        movie?.let {
            holder.showcaseBackdrop.setOnClickListener { showcaseListener.invoke(movie) }
            holder.showcaseBackdrop.load(context, movie.getFullBackdropPath())
            holder.showcaseTitle.text = movie.title
        }
        holder.showcaseCategory.text = showcases[position].title
        holder.viewMoreButton.setOnClickListener {
            viewMoreListener.invoke(showcases[position])
        }
    }

    override fun getItemCount(): Int = showcases.size

    class SpecialListViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val showcaseCategory by lazy { view.find<TextView>(R.id.showcase_category) }
        val showcaseBackdrop by lazy { view.find<ImageView>(R.id.showcase_backdrop) }
        val showcaseTitle by lazy { view.find<TextView>(R.id.showcase_title) }
        val viewMoreButton by lazy { view.find<Button>(R.id.see_all_button) }
    }
}