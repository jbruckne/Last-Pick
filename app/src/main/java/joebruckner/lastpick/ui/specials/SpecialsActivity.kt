package joebruckner.lastpick.ui.specials

import android.os.Bundle
import android.support.v4.app.Fragment
import joebruckner.lastpick.ActivityModule
import joebruckner.lastpick.MainApp
import joebruckner.lastpick.R
import joebruckner.lastpick.model.ListType
import joebruckner.lastpick.ui.common.BaseActivity
import joebruckner.lastpick.utils.replaceFrame
import joebruckner.lastpick.utils.setHomeAsUpEnabled

class SpecialsActivity : BaseActivity() {
    override val layoutId = R.layout.activity_specials

    val component by lazy {
        (application as MainApp)
                .component
                .getSpecialsComponent(ActivityModule(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val type = intent.extras.getInt("type", 0)

        title = ListType.values()[type].name
        setHomeAsUpEnabled(true)
        replaceFrame(R.id.frame, SpecialsFragment.newInstance(type))
    }

    override fun inject(fragment: Fragment) {
        when (fragment) {
            is SpecialsFragment -> component.inject(fragment)
            else -> super.inject(fragment)
        }
    }
}