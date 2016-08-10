package joebruckner.lastpick.ui.movie.adapters

import android.graphics.Color
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import joebruckner.lastpick.R
import joebruckner.lastpick.model.tmdb.Genre

class GenreAdapter(
        initSelected: BooleanArray,
        val listener: ((Int) -> Unit)? = null
): RecyclerView.Adapter<GenreAdapter.GenreViewHolder>() {
    val genres = Genre.getAll()
    var selected = initSelected

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder? {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.toggle_genre, parent, false)
        return GenreViewHolder(view)
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        holder.name.text = genres[position].name
        setToggle(holder.view, position)
        holder.view.setOnClickListener {
            listener?.invoke(position)
            selected[position] = !selected[position]
            setToggle(it, position)
        }
    }

    fun setToggle(view: View, position: Int) {
        val name = view.findViewById(R.id.name) as TextView
        val card = view.findViewById(R.id.card) as CardView
        val black = Color.parseColor("#212121")
        val accent = Color.parseColor("#1DE9B6")
        if (!selected[position]) {
            name.setTextColor(Color.WHITE)
            card.setCardBackgroundColor(black)
        } else {
            name.setTextColor(black)
            card.setCardBackgroundColor(accent)
        }

    }

    override fun getItemCount(): Int = genres.size - 1

    class GenreViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView

        init {
            name = view.findViewById(R.id.name) as TextView
        }
    }
}