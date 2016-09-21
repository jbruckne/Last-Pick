package joebruckner.lastpick.view.specials

import android.os.Bundle
import android.support.v4.app.Fragment
import joebruckner.lastpick.ActivityModule
import joebruckner.lastpick.MainApp
import joebruckner.lastpick.R
import joebruckner.lastpick.model.Showcase
import joebruckner.lastpick.view.common.BaseActivity
import joebruckner.lastpick.utilities.replaceFrame
import joebruckner.lastpick.utilities.setHomeAsUpEnabled

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

        title = Showcase.values()[type].title
        setHomeAsUpEnabled(true)
        replaceFrame(R.id.frame, SpecialsFragment.newInstance(type))
    }

    override fun inject(injectee: Any) {
        when (injectee) {
            is SpecialsFragment -> component.inject(injectee)
            else -> super.inject(injectee)
        }
    }
}