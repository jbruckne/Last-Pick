package joebruckner.lastpick.ui.movie.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import joebruckner.lastpick.ActivityScope
import joebruckner.lastpick.R
import joebruckner.lastpick.model.guidebox.Source
import joebruckner.lastpick.ui.movie.adapters.SourceAdapter.SourceViewHolder
import joebruckner.lastpick.utils.find
import joebruckner.lastpick.utils.inflate
import javax.inject.Inject
import javax.inject.Named

@ActivityScope
class SourceAdapter @Inject constructor(
        @Named("Activity") val context: Context
): RecyclerView.Adapter<SourceViewHolder>() {
    private var sources: List<Source> = listOf()
    var listener: (Source) -> Unit = {}
    var showAll = false
        set(value) {
            field = value
            if (sources.size < 4) return
            if (value) notifyItemRangeInserted(4, sources.size - 4)
            else notifyItemRangeRemoved(4, sources.size - 4)
        }

    fun setNewItems(items: List<Source>?) {
        if (items == null || sources.equals(items)) return
        sources = items
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: SourceViewHolder, position: Int) {
        val source = sources[position]
        holder.name.text = source.displayName
        holder.view.setOnClickListener {
            listener.invoke(source)
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(source.link)))
        }
    }

    override fun getItemCount() = if (showAll) sources.size else Math.min(sources.size, 4)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SourceViewHolder {
        return SourceViewHolder(parent.inflate(R.layout.card_source))
    }

    class SourceViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val name by lazy { view.find<TextView>(R.id.name) }
    }
}