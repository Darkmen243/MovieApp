package com.example.movieapp

import android.content.Context
import android.content.SharedPreferences

class PreferencesHelper(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("movie_preferences", Context.MODE_PRIVATE)

    fun getFilterGenre(): String {
        return sharedPreferences.getString("filter_genre", "All") ?: "All"
    }

    fun setFilterGenre(genre: String) {
        sharedPreferences.edit().putString("filter_genre", genre).apply()
    }

    fun getSortOrder(): String {
        return sharedPreferences.getString("sort_order", "title") ?: "title"
    }

    fun setSortOrder(order: String) {
        sharedPreferences.edit().putString("sort_order", order).apply()
    }

    fun setSelectedUserId(userId: Int) {
        sharedPreferences.edit().putInt("selected_user_id", userId).apply()
    }

    fun getSelectedUserId(): Int {
        return sharedPreferences.getInt("selected_user_id", -1)
    }


}
