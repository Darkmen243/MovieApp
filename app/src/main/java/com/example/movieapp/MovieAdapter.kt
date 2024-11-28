package com.example.movieapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class MovieAdapter(
    private val onDeleteClick: (Movie) -> Unit,
    private val onMarkAsWatchedClick: (Movie) -> Unit,
    private val watchedStatus: (Movie) -> LiveData<Boolean?>,
    private val lifecycleOwner: LifecycleOwner
) : ListAdapter<Movie, MovieAdapter.MovieViewHolder>(MovieDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = getItem(position)
        holder.bind(movie, onDeleteClick, onMarkAsWatchedClick, watchedStatus(movie), lifecycleOwner)
    }

    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        private val detailsTextView: TextView = itemView.findViewById(R.id.detailsTextView)
        private val deleteButton: Button = itemView.findViewById(R.id.deleteButton)
        private val markWatchedButton: Button = itemView.findViewById(R.id.markWatchedButton)
        private val watchedStatusTextView: TextView = itemView.findViewById(R.id.statusTextView)

        fun bind(
            movie: Movie,
            onDeleteClick: (Movie) -> Unit,
            onMarkAsWatchedClick: (Movie) -> Unit,
            watchedStatus: LiveData<Boolean?>,
            lifecycleOwner: LifecycleOwner
        ) {
            titleTextView.text = movie.title
            "Year: ${movie.year}, Genre: ${movie.genre}".also { detailsTextView.text = it }

            deleteButton.setOnClickListener { onDeleteClick(movie) }
            markWatchedButton.setOnClickListener {
                onMarkAsWatchedClick(movie)

            }

            watchedStatus.observe(lifecycleOwner) { isWatched ->
                watchedStatusTextView.text = if (isWatched == true) "Watched: Yes" else "Watched: No"
            }
        }
    }
}

class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean = oldItem == newItem
}
