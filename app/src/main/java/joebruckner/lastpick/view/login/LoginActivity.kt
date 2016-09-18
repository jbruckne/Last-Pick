package joebruckner.lastpick.view.login


import android.os.Bundle
import joebruckner.lastpick.R
import joebruckner.lastpick.utils.replaceFrame
import joebruckner.lastpick.view.common.BaseActivity

class LoginActivity : BaseActivity() {
    override val layoutId = R.layout.activity_login

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        replaceFrame(R.id.frame, LoginFragment())
    }
}
