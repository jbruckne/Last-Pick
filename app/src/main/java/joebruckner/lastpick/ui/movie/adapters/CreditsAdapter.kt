package joebruckner.lastpick.ui.movie.adapters

import android.content.Context
import android.support.v7.widget.AppCompatDrawableManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import joebruckner.lastpick.ActivityScope
import joebruckner.lastpick.R
import joebruckner.lastpick.model.tmdb.Cast
import joebruckner.lastpick.model.tmdb.Credits
import joebruckner.lastpick.model.tmdb.Crew
import joebruckner.lastpick.utils.load
import javax.inject.Inject
import javax.inject.Named

@ActivityScope
class CreditsAdapter @Inject constructor(
        @Named("Activity") val context: Context
): RecyclerView.Adapter<CreditsAdapter.CastViewHolder>() {
    var credits: Credits? = null
        set(value) {
            if (value == field) return
            field = value
            notifyDataSetChanged()
        }
    var castListener: (Cast) -> Unit = {}
    var crewListener: (Crew) -> Unit = {}

    override fun getItemCount(): Int {
        val castSize = credits?.cast?.size ?: 0
        val creditsSize = if (credits?.getDirector() == null) 0 else 1
        return if (castSize > 5) creditsSize + 5 else creditsSize + castSize
    }

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        holder.profilePicture.background = AppCompatDrawableManager
                .get()
                .getDrawable(context, R.drawable.ic_account_circle)
        if (position == 0 && credits?.getDirector() != null) {
            val crew = credits!!.crew[0]
            holder.name.text = crew.name
            holder.character.text = crew.job
            holder.profilePicture.load(context, crew.getFullProfilePath())
            holder.view.setOnClickListener { crewListener.invoke(crew) }
        } else {
            val adjustment = if (credits?.getDirector() == null) position else position - 1
            val cast = credits!!.cast[adjustment]
            holder.name.text = cast.name
            holder.character.text = cast.character
            holder.profilePicture.load(context, cast.getFullProfilePath())
            holder.view.setOnClickListener { castListener.invoke(cast) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder? {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_actor, parent, false)
        return CastViewHolder(view)
    }


    class CastViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val profilePicture: ImageView = view.findViewById(R.id.profile_picture) as ImageView
        val name: TextView = view.findViewById(R.id.name) as TextView
        val character: TextView = view.findViewById(R.id.character) as TextView
    }
}