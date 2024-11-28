package com.example.movieapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

class AddUserActivity : AppCompatActivity() {
    private val movieViewModel: MovieViewModel by viewModels {
        MovieViewModelFactory((application as MovieApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_user)

        val usernameEditText = findViewById<EditText>(R.id.usernameInput)
        val addUserButton = findViewById<Button>(R.id.saveUserButton)

        addUserButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            if (username.isNotEmpty()) {
                val user = User(username = username)
                movieViewModel.addUser(user)
                finish()
            } else {
                usernameEditText.error = "Username cannot be empty"
            }
        }
    }
}
