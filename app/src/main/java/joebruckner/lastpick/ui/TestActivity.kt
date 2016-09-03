package joebruckner.lastpick.ui

import android.os.Bundle
import com.joebruckner.androidbase.BaseActivity
import joebruckner.lastpick.R
import joebruckner.lastpick.utils.replaceFrame

class TestActivity : BaseActivity() {
    override val layoutId = R.layout.activity_test

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        replaceFrame(R.id.frame, TestFragment())
    }
}
