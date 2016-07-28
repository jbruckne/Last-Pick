package joebruckner.lastpick.ui.settings


import android.os.Bundle
import android.preference.PreferenceFragment
import android.support.v4.app.Fragment
import joebruckner.lastpick.R

/**
 * A simple [Fragment] subclass.
 */
class SettingsFragment : PreferenceFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.settings)
    }
}
