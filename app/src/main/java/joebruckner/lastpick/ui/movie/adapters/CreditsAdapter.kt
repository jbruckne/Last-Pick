package joebruckner.lastpick.ui.movie.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import joebruckner.lastpick.R
import joebruckner.lastpick.data.Credits
import joebruckner.lastpick.utils.load

class CreditsAdapter(val context: Context): RecyclerView.Adapter<CreditsAdapter.CastViewHolder>() {
    var credits: Credits? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int {
        val castSize = credits?.cast?.size ?: 0
        val creditsSize = if (credits?.getDirector() == null) 0 else 1
        return if (castSize > 5) creditsSize + 5 else creditsSize + castSize
    }

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        if (position == 0 && credits?.getDirector() != null) {
            val crew = credits!!.crew[0]
            holder.name.text = crew.name
            holder.character.text = crew.job
            holder.profilePicture.load(context, crew.getFullProfilePath())
        } else {
            val adjustment = if (credits?.getDirector() == null) position else position - 1
            val cast = credits!!.cast[adjustment]
            holder.name.text = cast.name
            holder.character.text = cast.character
            holder.profilePicture.load(context, cast.getFullProfilePath())
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