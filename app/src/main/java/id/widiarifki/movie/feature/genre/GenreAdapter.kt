package id.widiarifki.movie.feature.genre

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import id.widiarifki.movie.R
import id.widiarifki.movie.data.model.Genre
import id.widiarifki.movie.databinding.ItemGenreBinding

class GenreAdapter(val context: Context, val list: ArrayList<Genre>) : RecyclerView.Adapter<GenreViewHolder>() {

    var itemEventListener: ItemGenreListener? = null

    override fun getItemCount(): Int = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        val inflater = LayoutInflater.from(context)
        val binding: ItemGenreBinding = DataBindingUtil.inflate(inflater, R.layout.item_genre, parent, false)
        return GenreViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        val itemData = list[position]
        holder.bind(itemData)
        itemEventListener?.let { listener ->
            holder.binding.root.setOnClickListener {
                listener.onClickGenre(itemData)
            }
        }
    }

    interface ItemGenreListener {
        fun onClickGenre(data: Genre)
    }
}

class GenreViewHolder(val binding: ItemGenreBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(data: Genre) {
        binding.apply {
            this.data = data
        }
    }
}
