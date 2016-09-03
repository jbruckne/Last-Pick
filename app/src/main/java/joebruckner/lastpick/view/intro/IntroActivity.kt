package joebruckner.lastpick.ui.intro

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import com.github.paolorotolo.appintro.AppIntro2
import com.github.paolorotolo.appintro.AppIntro2Fragment
import joebruckner.lastpick.R

class IntroActivity: AppIntro2() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addSlide(AppIntro2Fragment.newInstance(
                "Test 1", "This is a test", R.drawable.ic_dice_1, Color.LTGRAY
        ))
        addSlide(AppIntro2Fragment.newInstance(
                "Test 2", "This is another test", R.drawable.ic_dice_3, Color.GRAY
        ))
        addSlide(AppIntro2Fragment.newInstance(
                "Test 3", "This is the last test", R.drawable.ic_dice_5, Color.DKGRAY
        ))

        showStatusBar(false)
        skipButtonEnabled = false
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        finish()
    }
}