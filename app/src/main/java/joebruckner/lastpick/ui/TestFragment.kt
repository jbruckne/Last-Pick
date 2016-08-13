package joebruckner.lastpick.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import joebruckner.lastpick.R
import joebruckner.lastpick.utils.find

class TestFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_movie_info, container, false)
        view.find<TextView>(R.id.overview).text = StringBuilder().padStart(1000, 'J').toString()
        return view
    }
}