package joebruckner.lastpick.ui.movie.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import joebruckner.lastpick.R
import joebruckner.lastpick.model.tmdb.Image
import joebruckner.lastpick.utils.*
import javax.inject.Inject
import javax.inject.Named

class ImageAdapter @Inject constructor(
        @Named("Activity") val context: Context
) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {
    var listener: ((Image, ImageView) -> Unit)? = null
    private var images: List<Image> = emptyList()
    private val backgrounds: List<Int>

    init {
        val workList = mutableListOf<Int>()
        with (context.resources) {
            workList.add(getColor(R.color.material_grey_800, null))
            workList.add(getColor(R.color.material_grey_850, null))
            workList.add(getColor(R.color.material_grey_900, null))
        }
        backgrounds = workList.toList()
    }

    fun setNewItems(posters: List<Image>, backdrops: List<Image>) {
        val list: MutableList<Image> = mutableListOf()
        for (i in 0..4) {
            if ((i.isEven() || backdrops.size < i/2) && posters.size > i/2) list.add(posters[i / 2])
            else if (backdrops.size > i/2) list.add(backdrops[i / 2])
            else break
        }
        if (images == list.toList()) return
        images = list.toList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(parent.inflate(viewType))
    }

    override fun getItemCount(): Int = images.size

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val image = images[position]
        holder.image.load(context, image.fullPath())
        holder.image.setBackgroundColor(backgrounds.shuffle().first())
        holder.view.setOnClickListener { listener?.invoke(image, holder.image) }
    }

    override fun getItemViewType(position: Int): Int {
        return when (images[position].isPoster()) {
            true -> R.layout.card_poster
            else -> R.layout.card_backdrop
        }
    }

    class ImageViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val image by lazy { view.find<ImageView>(R.id.image) }
    }
}