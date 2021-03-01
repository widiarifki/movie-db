package id.widiarifki.movie.presentation.movie.list

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import id.widiarifki.movie.R
import id.widiarifki.movie.data.model.Movie
import id.widiarifki.movie.databinding.ItemMovieBinding
import id.widiarifki.movie.presentation.movie.detail.MovieDetailActivity
import id.widiarifki.movie.utils.ParamConstant

class MoviePagingAdapter : PagingDataAdapter<Movie, MoviePagingAdapter.ItemMovieHolder>(DiffCallback()) {

    var itemListener: ItemMovieListener? = null

    override fun onBindViewHolder(holder: ItemMovieHolder, position: Int) {
        val viewBinding = holder.binding
        val data = getItem(position)

        data?.let {
            viewBinding.data = it
            itemListener?.let { customAction ->
                viewBinding.root.setOnClickListener {
                    customAction.onClickMovie(data)
                }
            } ?: kotlin.run {
                viewBinding.root.setOnClickListener {
                    onClickMovie(data, holder.binding.root.context)
                }
            }
        }
    }

    private fun onClickMovie(data: Movie, context: Context) {
        val intent = Intent(context, MovieDetailActivity::class.java)
        intent.putExtra(ParamConstant.MOVIE_ID, data.id)
        intent.putExtra(ParamConstant.MOVIE_NAME, data.title)
        (context as? Activity)?.startActivity(intent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemMovieHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return ItemMovieHolder(ItemMovieBinding.bind(view))
    }

    class ItemMovieHolder(val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root)

    interface ItemMovieListener {
        fun onClickMovie(data: Movie?)
    }

    /**
     * DiffCallback required by paging adapter,
     * so it can compare between 2 data objects while managing the adapter
     */
    class DiffCallback: DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }
}