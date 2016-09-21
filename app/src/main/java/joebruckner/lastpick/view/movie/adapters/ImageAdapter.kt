package joebruckner.lastpick.view.movie.adapters

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import joebruckner.lastpick.R
import joebruckner.lastpick.model.tmdb.Image
import joebruckner.lastpick.utilities.*
import joebruckner.lastpick.utils.*
import javax.inject.Inject
import javax.inject.Named

class ImageAdapter @Inject constructor(
        @Named("Activity") val context: Context
) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {
    var listener: ((Image, ImageView) -> Unit)? = null
    private var images: List<Image> = emptyList()
    private val backgrounds: List<Int>
    var showAll = false
        set(value) {
            field = value
            if (images.size <= 5) return
            if (value) notifyItemRangeInserted(5, images.size - 5)
            else notifyItemRangeRemoved(5, images.size - 5)
        }

    init {
        val workList = mutableListOf<Int>()
        workList.add(ContextCompat.getColor(context, R.color.material_grey_800))
        workList.add(ContextCompat.getColor(context, R.color.material_grey_850))
        workList.add(ContextCompat.getColor(context, R.color.material_grey_900))
        backgrounds = workList.toList()
    }

    fun setNewItems(posters: List<Image>, backdrops: List<Image>) {
        val pList = posters.toMutableList()
        val bList = backdrops.toMutableList()
        val list: MutableList<Image> = mutableListOf()
        for (i in 0..Math.max(posters.size, backdrops.size)) {
            when (true) {
                i.isEven() && pList.size > 0 -> list.add(pList.removeAt(0))
                else -> if (bList.size > 0) list.add(bList.removeAt(0))
            }
        }
        images = list.toList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(parent.inflate(viewType))
    }

    override fun getItemCount() = if (showAll) images.size else Math.min(images.size, 5)

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val image = images[position]
        holder.image.load(context, image.fullPath())
        holder.image.setBackgroundColor(backgrounds.shuffle().first())
        holder.view.setOnClickListener { listener?.invoke(image, holder.image) }
    }

    override fun getItemViewType(position: Int): Int {
        return when (images[position].isPoster()) {
            true -> R.layout.item_poster
            else -> R.layout.item_backdrop
        }
    }

    class ImageViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val image by lazy { view.find<ImageView>(R.id.image) }
    }
}