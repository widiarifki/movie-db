package id.widiarifki.movie.utils.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import id.widiarifki.movie.R
import id.widiarifki.movie.databinding.BaseLoadingErrorBinding

class PagingLoadStateAdapter(private val retryCallback: () -> Unit)
    : LoadStateAdapter<PagingLoadStateAdapter.LoadStateViewHolder>() {

    class LoadStateViewHolder(val binding: BaseLoadingErrorBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        val viewBinding = holder.binding

        with(loadState) {
            viewBinding.isLoading = this is LoadState.Loading
            (this as? LoadState.Error)?.error?.message?.let {
                viewBinding.message = it
            }
        }

        viewBinding.btnRetry.setOnClickListener {
            retryCallback.invoke()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.base_loading_error, parent, false)
        return LoadStateViewHolder(BaseLoadingErrorBinding.bind(view))
    }
}