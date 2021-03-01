package id.widiarifki.movie.presentation.movie.list

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import id.widiarifki.movie.R
import id.widiarifki.movie.base.BaseActivity
import id.widiarifki.movie.databinding.ActivityMovieListBinding
import id.widiarifki.movie.utils.ParamConstant
import id.widiarifki.movie.utils.ui.PagingLoadStateAdapter
import id.widiarifki.movie.utils.ui.SpacedItemDecoration
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieListActivity : BaseActivity<ActivityMovieListBinding>() {

    private val viewModel: MovieListViewModel by viewModels()
    private val movieAdapter = MoviePagingAdapter()
    private var genreId: Int? = null

    override val resourceLayout: Int
        get() = R.layout.activity_movie_list

    override fun onViewReady(savedInstanceState: Bundle?) {
        handleIntent()
        setupRecyclerView()
        setupListener()
        observeData()
    }

    private fun handleIntent() {
        val extras = intent.extras
        setupToolbar(extras?.getString(ParamConstant.GENRE_NAME), true)
        extras?.getInt(ParamConstant.GENRE_ID)?.let {
            genreId = it
        }
    }

    private fun setupRecyclerView() {
        binding.rvMovie.apply {
            val context = this@MovieListActivity
            layoutManager = LinearLayoutManager(context)
            adapter = movieAdapter.withLoadStateFooter(PagingLoadStateAdapter{
                movieAdapter.retry()
            })
            addItemDecoration(SpacedItemDecoration(context))
        }
    }

    private fun setupListener() {
        binding.refreshLayout.setOnRefreshListener {
            movieAdapter.refresh()
        }
        binding.layoutLoadingError.btnRetry.setOnClickListener {
            movieAdapter.retry()
        }
    }

    private fun observeData() {
        lifecycleScope.launch {
            viewModel.getMovies(genreId).collectLatest { movies ->
                movieAdapter.submitData(movies)
            }
        }

        lifecycleScope.launch {
            movieAdapter.loadStateFlow.collectLatest { state ->
                with(state.refresh) {
                    binding.refreshLayout.isRefreshing = this is LoadState.NotLoading && binding.refreshLayout.isRefreshing
                    binding.isLoading = this is LoadState.Loading && movieAdapter.itemCount == 0
                    binding.isError = (this is LoadState.Error || this is LoadState.NotLoading) && movieAdapter.itemCount == 0
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
