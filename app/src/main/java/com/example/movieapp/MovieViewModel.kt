package com.example.movieapp

import androidx.lifecycle.*
import kotlinx.coroutines.launch

class MovieViewModel(private val repository: MovieRepository) : ViewModel() {
    val allMovies: LiveData<List<Movie>> = repository.movies
    val allUsers: LiveData<List<User>> = repository.users

    fun refreshMovies() {
        viewModelScope.launch {
            repository.refreshMovies()
        }
    }

    fun deleteMovie(movie: Movie) {
        viewModelScope.launch {
            repository.deleteMovie(movie)
        }
    }

    fun insertMovie(movie: Movie) {
        viewModelScope.launch {
            repository.insertMovie(movie)
        }
    }


    fun getMovieStatus(userId: Int, movie: Movie): LiveData<Boolean?> {
        return liveData {
            val status = repository.getMovieStatus(userId, movie.id)
            emit(status?.isWatched)
        }
    }


    fun addUser(user: User) {
        viewModelScope.launch {
            repository.addUser(user)
        }
    }

    fun markMovieAsWatchedForUser(userId: Int, movie: Movie) {
        viewModelScope.launch {
            val movieStatus = MovieStatus(
                movieId = movie.id,
                userId = userId,
                isWatched = true,
                personalRating = 5.0,
                watchDate = System.currentTimeMillis().toString()
            )
            repository.insertMovieStatus(userId, movieStatus)
            refreshMovies()
            getMovieStatus(userId,movie)
        }
    }
}

class MovieViewModelFactory(private val repository: MovieRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MovieViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
