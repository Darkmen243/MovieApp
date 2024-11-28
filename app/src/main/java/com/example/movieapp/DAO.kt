package com.example.movieapp

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MovieDao {
    @Insert
    suspend fun insert(movie: Movie)

    @Delete
    suspend fun delete(movie: Movie)

    @Query("SELECT * FROM movies")
    fun getAllMovies(): LiveData<List<Movie>>
}

@Dao
interface MovieStatusDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieStatus(movieStatus: MovieStatus)

    @Query("SELECT * FROM movie_status WHERE movieId = :movieId AND userId = :userId")
    suspend fun getMovieStatus(userId: Int, movieId: Int): MovieStatus?
    @Delete
    suspend fun deleteMovieStatus(movieStatus: MovieStatus)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movieStatus: MovieStatus)
    @Update
    suspend fun update(movieStatus: MovieStatus)
    @Query("SELECT * FROM movie_status")
    fun getAllMovieStatuses(): LiveData<List<MovieStatus>>
}

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: User)

    @Query("SELECT * FROM users")
    fun getAllUsers(): LiveData<List<User>>
    @Delete
    suspend fun deleteUser(user: User)
}


