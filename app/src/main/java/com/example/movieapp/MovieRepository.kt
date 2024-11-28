package com.example.movieapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MovieRepository(
    private val movieDao: MovieDao,
    private val movieStatusDao: MovieStatusDao,
    private val userDao: UserDao,
    private val preferencesHelper: PreferencesHelper
) {
    private val _movies = MediatorLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> get() = _movies
    val users: LiveData<List<User>> = userDao.getAllUsers()

    init {
        _movies.addSource(movieDao.getAllMovies()) { movies ->
            applyPreferences(movies)
        }
    }

    private fun applyPreferences(movies: List<Movie>) {
        val filterGenre = preferencesHelper.getFilterGenre().trim()
        val sortOrder = preferencesHelper.getSortOrder().lowercase()

        val filteredMovies = if (filterGenre == "All" || filterGenre.isEmpty()) {
            movies
        } else {
            movies.filter { it.genre.equals(filterGenre, ignoreCase = true) }
        }

        val sortedMovies = when (sortOrder) {
            "year" -> filteredMovies.sortedBy { it.year }
            "title" -> filteredMovies.sortedBy { it.title.lowercase() }
            else -> filteredMovies
        }

        _movies.postValue(sortedMovies)
    }


    suspend fun insertMovie(movie: Movie) = withContext(Dispatchers.IO) {
        movieDao.insert(movie)
    }

    suspend fun deleteMovie(movie: Movie) = withContext(Dispatchers.IO) {
        movieDao.delete(movie)
    }

    fun refreshMovies() {
        _movies.addSource(movieDao.getAllMovies()) { movies ->
            applyPreferences(movies)
        }
    }


    suspend fun getMovieStatus(userId: Int, movieId: Int): MovieStatus? {
        return movieStatusDao.getMovieStatus(userId, movieId)
    }

    suspend fun addUser(user: User) = withContext(Dispatchers.IO) {
        userDao.insert(user)
    }

    suspend fun insertMovieStatus(userId: Int, movieStatus: MovieStatus) {
        val existingStatus = movieStatusDao.getMovieStatus(userId, movieStatus.movieId)
        if (existingStatus == null) {
            movieStatusDao.insert(movieStatus)
        } else {
            movieStatusDao.update(movieStatus.copy(isWatched = true))
        }
    }


}
