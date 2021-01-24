package id.widiarifki.movie.feature.movie.list

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import id.widiarifki.movie.R
import id.widiarifki.movie.data.model.Movie
import id.widiarifki.movie.databinding.ItemMovieBinding

class MovieAdapter(val context: Context) : PagedListAdapter<Movie, MovieViewHolder>(DiffCallback) {

    var itemEventListener: ItemMovieListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val inflater = LayoutInflater.from(context)
        val binding: ItemMovieBinding = DataBindingUtil.inflate(inflater, R.layout.item_movie, parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val itemData = getItem(position)
        itemData?.let {
            holder.bind(it)
            itemEventListener?.let { listener ->
                holder.binding.root.setOnClickListener {
                    listener.onClickMovie(itemData)
                }
            }
        }
    }

    interface ItemMovieListener {
        fun onClickMovie(data: Movie)
    }

    object DiffCallback: DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }
}

class MovieViewHolder(val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(data: Movie) {
        binding.data = data
    }
}
