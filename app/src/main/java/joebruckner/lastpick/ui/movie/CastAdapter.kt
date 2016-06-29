package joebruckner.lastpick.ui.movie

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import joebruckner.lastpick.R
import joebruckner.lastpick.data.Cast

class CastAdapter(val context: Context): RecyclerView.Adapter<CastAdapter.CastViewHolder>() {
    var cast: List<Cast> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = cast.size

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        holder.name.text = cast[position].name
        Glide.with(context)
                .load(cast[position].getProfilepath())
                .centerCrop()
                .into(holder.profilePicture)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder? {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_actor, parent, false)
        return CastViewHolder(view)
    }


    class CastViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val profilePicture: ImageView = view.findViewById(R.id.profile_picture) as ImageView
        val name: TextView = view.findViewById(R.id.name) as TextView
    }
}