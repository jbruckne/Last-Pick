package joebruckner.lastpick.utils

import joebruckner.lastpick.model.tmdb.Image

fun Image.isPoster(): Boolean = height > width

fun Image.fullPath(): String = "http://image.tmdb.org/t/p/original/$filePath"