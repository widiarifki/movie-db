package id.widiarifki.movie.presentation.review

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import id.widiarifki.movie.R
import id.widiarifki.movie.data.model.Review
import id.widiarifki.movie.databinding.ItemReviewBinding

class ReviewPagingAdapter : PagingDataAdapter<Review, ReviewPagingAdapter.ItemReviewHolder>(DiffUtilCallback()) {

    override fun onBindViewHolder(holder: ItemReviewHolder, position: Int) {
        holder.binding.data = getItem(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemReviewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_review, parent, false)
        return ItemReviewHolder(ItemReviewBinding.bind(view))
    }

    class ItemReviewHolder(val binding: ItemReviewBinding) : RecyclerView.ViewHolder(binding.root)

    class DiffUtilCallback : DiffUtil.ItemCallback<Review>() {
        override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean {
            return oldItem.id == newItem.id
        }

    }

}
