package id.widiarifki.movie.presentation.watchlist

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import id.widiarifki.movie.R
import id.widiarifki.movie.base.BaseActivity
import id.widiarifki.movie.databinding.ActivityWatchlistBinding
import id.widiarifki.movie.presentation.movie.list.MoviePagingAdapter
import id.widiarifki.movie.utils.ui.PagingLoadStateAdapter
import id.widiarifki.movie.utils.ui.SpacedItemDecoration
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WatchlistActivity : BaseActivity<ActivityWatchlistBinding>() {

    private val viewModel: WatchlistViewModel by viewModels()
    private val listAdapter = MoviePagingAdapter()

    override val resourceLayout: Int = R.layout.activity_watchlist

    override fun onViewReady(savedInstanceState: Bundle?) {
        setupToolbar(getString(R.string.watchlist), true)
        setupRecyclerView()
        setupListener()
        observeData()
    }

    private fun setupRecyclerView() {
        binding.rvWatchlist.apply {
            adapter = listAdapter.withLoadStateFooter(
                PagingLoadStateAdapter {
                    listAdapter.retry()
                }
            )
            layoutManager = LinearLayoutManager(this@WatchlistActivity)
            addItemDecoration(SpacedItemDecoration(this@WatchlistActivity))
        }
    }

    private fun setupListener() {
        binding.refreshLayout.setOnRefreshListener {
            listAdapter.refresh()
        }
        binding.layoutLoadingError.btnRetry.setOnClickListener {
            listAdapter.retry()
        }
    }

    private fun observeData() {
        lifecycleScope.launch {
            viewModel.getPagingWatchlist().collectLatest {
                listAdapter.submitData(it)
            }
        }

        lifecycleScope.launch {
            listAdapter.loadStateFlow.collectLatest { state ->
                with(state.refresh) {
                    binding.refreshLayout.isRefreshing = this is LoadState.NotLoading && binding.refreshLayout.isRefreshing
                    binding.isLoading = this is LoadState.Loading && listAdapter.itemCount < 0
                    binding.isError = (this is LoadState.Error || this is LoadState.NotLoading) && listAdapter.itemCount < 0
                    (this as? LoadState.Error)?.error?.message?.let {
                        binding.message = it
                    } ?: run {
                        binding.message = getString(R.string.msg_empty_movie)
                    }
                }
            }
        }
    }


}