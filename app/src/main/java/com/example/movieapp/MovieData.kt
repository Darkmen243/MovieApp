package com.example.movieapp

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val year: Int,
    val genre: String,
    val country: String,
    val director: String
)

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val username: String
)

@Entity(
    tableName = "movie_status",
    foreignKeys = [
        ForeignKey(
            entity = Movie::class,
            parentColumns = ["id"],
            childColumns = ["movieId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["movieId"]), Index(value = ["userId"])]
)
data class MovieStatus(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val movieId: Int,
    val isWatched: Boolean,
    val personalRating: Double,
    val watchDate: String?
)
