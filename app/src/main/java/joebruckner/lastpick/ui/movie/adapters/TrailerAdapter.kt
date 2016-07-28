package joebruckner.lastpick.ui.movie.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import joebruckner.lastpick.R
import joebruckner.lastpick.data.Video
import joebruckner.lastpick.utils.find
import joebruckner.lastpick.utils.inflate
import joebruckner.lastpick.utils.load

class TrailerAdapter(val context: Context):
        RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder>() {
    var videos: List<Video> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onBindViewHolder(holder: TrailerViewHolder, position: Int) {
        val video = videos[position]
        holder.thumbnail.setImageDrawable(null)
        holder.thumbnail.load(context, video.getThumbnailUrl())
        holder.title.text = video.name
        holder.thumbnail.setOnClickListener {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(video.getTrailerUrl())))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrailerViewHolder {
        val view = parent.inflate(R.layout.card_trailer)
        return TrailerViewHolder(view)
    }

    override fun getItemCount() = videos.size

    class TrailerViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val thumbnail = view.find<ImageView>(R.id.trailer)
        val title = view.find<TextView>(R.id.trailer_title)
    }
}