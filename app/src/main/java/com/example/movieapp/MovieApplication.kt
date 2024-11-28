package com.example.movieapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MovieApplication : Application() {

    private val database by lazy { MovieDatabase.getDatabase(this) }
    private val preferencesHelper by lazy { PreferencesHelper(this) }
    val repository by lazy { MovieRepository(database.movieDao(), database.movieStatusDao(),database.userDao(), preferencesHelper) }
}
