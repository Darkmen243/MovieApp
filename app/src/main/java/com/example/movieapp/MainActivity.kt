package com.example.movieapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private val movieViewModel: MovieViewModel by viewModels {
        MovieViewModelFactory((application as MovieApplication).repository)
    }
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var preferencesHelper: PreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        preferencesHelper = PreferencesHelper(this)

        val recyclerView = findViewById<RecyclerView>(R.id.movieRecyclerView)
        movieAdapter = MovieAdapter(
            onDeleteClick = { movie -> movieViewModel.deleteMovie(movie) },
            onMarkAsWatchedClick = { movie ->
                movieViewModel.markMovieAsWatchedForUser(preferencesHelper.getSelectedUserId(), movie)
            },
            watchedStatus = { movie ->
                movieViewModel.getMovieStatus(preferencesHelper.getSelectedUserId(), movie)
            },
            lifecycleOwner = this
        )
        recyclerView.adapter = movieAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val sortSpinner = findViewById<Spinner>(R.id.sortSpinner)
        val filterSpinner = findViewById<Spinner>(R.id.filterSpinner)
        val userSpinner = findViewById<Spinner>(R.id.userSpinner)

        setupSpinners(sortSpinner, filterSpinner)
        setupUserSpinner(userSpinner)

        movieViewModel.allMovies.observe(this) { movies ->
            movieAdapter.submitList(movies)
            movieAdapter.notifyDataSetChanged()
        }

        findViewById<Button>(R.id.addMovieButton).setOnClickListener {
            startActivity(Intent(this, AddMovieActivity::class.java))
        }

        findViewById<Button>(R.id.addUserButton).setOnClickListener {
            startActivity(Intent(this, AddUserActivity::class.java))
        }
    }

    private fun setupSpinners(sortSpinner: Spinner, filterSpinner: Spinner) {
        val sortOptions = arrayOf("title", "year")
        val genreOptions = arrayOf("All", "Action", "Comedy", "Drama", "Horror")

        sortSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, sortOptions).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        filterSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, genreOptions).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        sortSpinner.setSelection(sortOptions.indexOf(preferencesHelper.getSortOrder()))
        filterSpinner.setSelection(genreOptions.indexOf(preferencesHelper.getFilterGenre()))

        sortSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                preferencesHelper.setSortOrder(sortOptions[position])
                movieViewModel.refreshMovies()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        filterSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                preferencesHelper.setFilterGenre(genreOptions[position])
                movieViewModel.refreshMovies()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setupUserSpinner(userSpinner: Spinner) {
        movieViewModel.allUsers.observe(this) { users ->
            val userNames = users.map { it.username }
            userSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, userNames).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }

            val selectedUserId = preferencesHelper.getSelectedUserId()
            userSpinner.setSelection(users.indexOfFirst { it.id == selectedUserId })

            userSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    preferencesHelper.setSelectedUserId(users[position].id)
                    movieViewModel.refreshMovies()
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }
    }
}
