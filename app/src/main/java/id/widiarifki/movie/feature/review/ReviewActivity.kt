package id.widiarifki.movie.feature.review

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.widiarifki.movie.R
import id.widiarifki.movie.base.BaseActivity
import id.widiarifki.movie.databinding.ActivityReviewBinding
import id.widiarifki.movie.helper.Constant

class ReviewActivity : BaseActivity<ActivityReviewBinding>() {

    private lateinit var viewModel: ReviewViewModel
    private var reviewAdapter: ReviewAdapter? = null

    override val resourceLayout: Int
        get() = R.layout.activity_review

    override fun onViewReady(savedInstanceState: Bundle?) {
        val extras = intent.extras
        setupToolbar(String.format("Review: %s", extras?.getString(Constant.PARAM_MOVIE_NAME)), true)
        viewModel = ViewModelProvider(this).get(ReviewViewModel::class.java)
        extras?.getInt(Constant.PARAM_MOVIE_ID)?.let {
            viewModel.setMovieId(it)
        }
        setupViewList()
        setupViewListener()
        observeData()
    }

    private fun setupViewList() {
        reviewAdapter = ReviewAdapter(this)
        binding.rvReview.apply {
            layoutManager = LinearLayoutManager(this@ReviewActivity)
            adapter = reviewAdapter
        }
    }

    private fun setupViewListener() {
        binding.layoutLoadingError.btnRetry.setOnClickListener {
            viewModel.retry()
        }
    }

    private fun observeData() {
        viewModel.livePagedList.observe(this, Observer {
            reviewAdapter?.submitList(it)
        })
        viewModel.liveRequestState.observe(this, Observer {
            when {
                it.isLoading() -> if (it.requestPage == 1) binding.isLoading = true
                it.isSuccess() -> {
                    binding.isLoading = false
                    binding.isError = false
                }
                it.isLoadEmpty() -> {
                    if (it.requestPage == 1) {
                        binding.isLoading = false
                        binding.isError = true
                        binding.message = "Belum ada yang mereview film ini"
                    }
                }
                it.isFail() -> {
                    binding.isLoading = false
                    if (it.requestPage == 1) {
                        binding.isError = true
                        binding.message = it.message
                    } else {
                        binding.isError = false
                        showToast(it.message)
                    }
                }
            }
        })
    }
}