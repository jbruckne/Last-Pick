package joebruckner.lastpick.ui.movie.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import joebruckner.lastpick.R
import joebruckner.lastpick.model.guidebox.Source
import joebruckner.lastpick.ui.movie.adapters.SourceAdapter.SourceViewHolder
import joebruckner.lastpick.utils.find
import joebruckner.lastpick.utils.inflate

class SourceAdapter(val context: Context): RecyclerView.Adapter<SourceViewHolder>() {
    private val sources: MutableList<Source> = mutableListOf()
    var listener: (Source) -> Unit = {}

    fun setNewItems(items: List<Source>?) {
        if (items == null) return
        sources.clear()
        sources.addAll(items)
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

    override fun getItemCount() = sources.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SourceViewHolder {
        return SourceViewHolder(parent.inflate(R.layout.card_source))
    }

    class SourceViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val name by lazy { view.find<TextView>(R.id.name) }
    }
}