package joebruckner.lastpick.source.bookmark

import joebruckner.lastpick.source.DatabaseHelper

object BookmarkEntry {
    val COLUMN_MOVIE = "GuideboxMovie"
    val COLUMN_MOVIE_ID = "MovieId"
    val TABLE_BOOKMARKS = "Bookmarks"
    val CREATE_TABLE_BOOKMARKS = "CREATE TABLE $TABLE_BOOKMARKS (" +
            "${DatabaseHelper.COLUMN_ID} INTEGER PRIMARY KEY, " +
            "$COLUMN_MOVIE_ID INTEGER, " +
            "$COLUMN_MOVIE TEXT);"
    val DELETE_TABLE_BOOKMARKS = "DROP TABLE IF EXISTS $TABLE_BOOKMARKS"
}