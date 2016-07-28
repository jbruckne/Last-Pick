package joebruckner.lastpick.sources.movie

import joebruckner.lastpick.sources.DatabaseHelper

object MovieEntry {
    val COLUMN_MOVIE_ID = "MovieId"
    val COLUMN_VALUE = "Value"
    val TABLE_MOVIES = "Movies"
    val CREATE_TABLE_MOVIES = "CREATE TABLE $TABLE_MOVIES (" +
            "${DatabaseHelper.COLUMN_ID} INTEGER PRIMARY KEY, " +
            "$COLUMN_MOVIE_ID INTEGER, " +
            "$COLUMN_VALUE TEXT" +
            ");"
    val DELETE_TABLE_MOVIES = "DROP TABLE IF EXISTS $TABLE_MOVIES"
}