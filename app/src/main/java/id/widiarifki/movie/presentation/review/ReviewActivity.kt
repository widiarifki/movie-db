package id.widiarifki.movie.presentation.review

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import id.widiarifki.movie.R
import id.widiarifki.movie.base.BaseActivity
import id.widiarifki.movie.databinding.ActivityReviewBinding
import id.widiarifki.movie.utils.Constant
import id.widiarifki.movie.utils.ui.PagingLoadStateAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ReviewActivity : BaseActivity<ActivityReviewBinding>() {

    private val viewModel: ReviewViewModel by viewModels()
    private val reviewAdapter = ReviewPagingAdapter()
    private var movieId: Int? = null

    override val resourceLayout: Int = R.layout.activity_review

    override fun onViewReady(savedInstanceState: Bundle?) {
        handleIntent()
        setupRecyclerView()
        setupListener()
        observeData()
    }

    private fun handleIntent() {
        val extras = intent.extras
        setupToolbar(String.format("Review: %s", extras?.getString(Constant.PARAM_MOVIE_NAME)), true)
        extras?.getInt(Constant.PARAM_MOVIE_ID)?.let {
            movieId = it
        }
    }

    private fun setupRecyclerView() {
        binding.rvReview.apply {
            layoutManager = LinearLayoutManager(this@ReviewActivity)
            adapter = reviewAdapter.withLoadStateFooter(
                PagingLoadStateAdapter {
                    reviewAdapter.retry()
                }
            )
        }
    }

    private fun setupListener() {
        binding.layoutLoadingError.btnRetry.setOnClickListener {
            reviewAdapter.retry()
        }
    }

    private fun observeData() {
        lifecycleScope.launch {
            viewModel.getReviews(movieId).collectLatest {
                reviewAdapter.submitData(it)
            }
        }

        lifecycleScope.launch {
            reviewAdapter.loadStateFlow.collectLatest { state ->
                with(state.refresh) {
                    binding.isLoading = this is LoadState.Loading && reviewAdapter.itemCount == 0
                    binding.isError = (this is LoadState.Error || this is LoadState.NotLoading) && reviewAdapter.itemCount == 0
                    (this as? LoadState.Error)?.error?.message?.let {
                        binding.message = it
                    } ?: run {
                        binding.message = getString(id.widiarifki.movie.R.string.msg_empty_movie)
                    }
                }
            }
        }
    }
}