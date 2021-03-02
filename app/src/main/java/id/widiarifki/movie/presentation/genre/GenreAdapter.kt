package id.widiarifki.movie.presentation.genre

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.qualifiers.ApplicationContext
import id.widiarifki.movie.R
import id.widiarifki.movie.data.model.Genre
import id.widiarifki.movie.databinding.ItemGenreBinding
import javax.inject.Inject

class GenreAdapter(val list: ArrayList<Genre>) : RecyclerView.Adapter<GenreAdapter.ItemGenreHolder>() {

    var itemListener: ItemGenreListener? = null

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ItemGenreHolder, position: Int) {
        val viewBinding = holder.binding
        val data = list[position]
        // Pass data to view
        viewBinding.data = data
        // Define item view listener
        itemListener?.let { listener ->
            viewBinding.root.setOnClickListener {
                listener.onClickGenre(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemGenreHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_genre, parent, false)
        return ItemGenreHolder(ItemGenreBinding.bind(view))
    }

    fun withListener(listener: ItemGenreListener): GenreAdapter {
        itemListener = listener
        return this
    }

    interface ItemGenreListener {
        fun onClickGenre(data: Genre)
    }

    class ItemGenreHolder(val binding: ItemGenreBinding) : RecyclerView.ViewHolder(binding.root)
}