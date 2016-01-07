package joebruckner.lastpick.ui.home

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.MenuItem
import joebruckner.lastpick.R
import joebruckner.lastpick.data.Genre
import joebruckner.lastpick.ui.bookmarks.BookmarksActivity
import joebruckner.lastpick.ui.common.BaseActivity
import joebruckner.lastpick.ui.history.HistoryActivity
import java.util.*

class MovieShuffleActivity : BaseActivity() {
    override val layoutId = R.layout.activity_movie_shuffle
    override val menuId = R.menu.menu_shuffle_movie

    val genres = Genre.getAll()
    val bools = BooleanArray(genres.size)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Home"

        replaceFrame(R.id.frame, MovieFragment(), false)
    }

    override fun onStart() {
        super.onStart()
        fab?.setOnClickListener {
            (getFragment(R.id.frame) as MovieFragment).callForUpdate()
            fab?.setImageResource(when (Random().nextInt(5)) {
                1 -> R.drawable.ic_dice_one_48dp
                2 -> R.drawable.ic_dice_two_48dp
                3 -> R.drawable.ic_dice_three_48dp
                4 -> R.drawable.ic_dice_four_48dp
                5 -> R.drawable.ic_dice_five_48dp
                else -> R.drawable.ic_dice_six_48dp
            })
        }

        if (isFirstStart) (getFragment(R.id.frame) as MovieFragment).callForUpdate()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_filter -> {
                showFilterDialog()
                return true
            }
            R.id.action_history -> {
                startActivity(Intent(this, javaClass<HistoryActivity>()))
                return true
            }
            R.id.action_view_bookmarks -> {
                startActivity(Intent(this, javaClass<BookmarksActivity>()))
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    fun showFilterDialog() {
        val selectedGenres = arrayListOf<Genre>()
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Genres")
                .setMultiChoiceItems(
                genres.map{ it.name }.toTypedArray(),
                bools,
                { dialog, which, isChecked -> bools[which] = isChecked })
                .setOnDismissListener { dialog -> dialog.dismiss() }
                .setNegativeButton("Cancel", { dialog, id -> dialog.cancel() })
                .setPositiveButton("Save", { dialog, id ->
                    for ((i, bool) in bools.withIndex()) {
                        if (bool) selectedGenres.add(genres[i])
                    }
                    Log.d(logTag, selectedGenres.toString())
                    val fragment = supportFragmentManager.findFragmentById(R.id.frame)
                    if (fragment is MovieFragment) {
                        if (selectedGenres.isEmpty()) {
                            fragment.filter = null
                        } else if (selectedGenres.first().id == 0) {
                            genres.drop(0)
                            fragment.filter = genres
                        } else {
                            fragment.filter = selectedGenres
                        }
                    }
                }).create().show()
    }
}