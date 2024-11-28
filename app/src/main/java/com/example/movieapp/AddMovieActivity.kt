package com.example.movieapp

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

class AddMovieActivity : AppCompatActivity() {

    private val movieViewModel: MovieViewModel by viewModels {
        MovieViewModelFactory((application as MovieApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_movie)

        val titleEditText = findViewById<EditText>(R.id.titleEditText)
        val yearEditText = findViewById<EditText>(R.id.yearEditText)
        val countryEditText = findViewById<EditText>(R.id.countryEditText)
        val directorEditText = findViewById<EditText>(R.id.directorEditText)
        val genreSpinner = findViewById<Spinner>(R.id.genreSpinner)

        val genres = arrayOf("Action", "Comedy", "Drama", "Horror")
        genreSpinner.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            genres
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        val saveButton = findViewById<Button>(R.id.saveButton)
        saveButton.setOnClickListener {
            val title = titleEditText.text.toString()
            val year = yearEditText.text.toString().toIntOrNull() ?: 0
            val genre = genreSpinner.selectedItem.toString()
            val country = countryEditText.text.toString()
            val director = directorEditText.text.toString()

            if (title.isNotBlank() && year > 0) {
                val movie = Movie(
                    title = title,
                    year = year,
                    genre = genre,
                    country = country,
                    director = director
                )
                movieViewModel.insertMovie(movie)
                Toast.makeText(this, "Movie added successfully", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
