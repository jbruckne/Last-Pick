package joebruckner.lastpick.ui.specials

import android.os.Bundle
import joebruckner.lastpick.R
import joebruckner.lastpick.data.ListType
import joebruckner.lastpick.ui.common.BaseActivity
import joebruckner.lastpick.utils.replaceFrame
import joebruckner.lastpick.utils.setHomeAsUpEnabled

class SpecialsActivity : BaseActivity() {
    override val layoutId = R.layout.activity_specials

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val type = intent.extras.getInt("type", 0)

        title = ListType.values()[type].title
        setHomeAsUpEnabled(true)
        replaceFrame(R.id.frame, SpecialsFragment.newInstance(type))
    }
}
