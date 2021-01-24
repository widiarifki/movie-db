package id.widiarifki.movie.feature.movie.list

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.widiarifki.movie.R
import id.widiarifki.movie.base.BaseActivity
import id.widiarifki.movie.data.model.Movie
import id.widiarifki.movie.databinding.ActivityMovieListBinding
import id.widiarifki.movie.feature.movie.detail.MovieDetailActivity
import id.widiarifki.movie.helper.Constant
import id.widiarifki.movie.helper.ui.SpacedItemDecoration

class MovieListActivity : BaseActivity<ActivityMovieListBinding>(), MovieAdapter.ItemMovieListener {

    private lateinit var viewModel: MovieListViewModel
    private var movieAdapter: MovieAdapter? = null

    override val resourceLayout: Int
        get() = R.layout.activity_movie_list

    override fun onViewReady(savedInstanceState: Bundle?) {
        val extras = intent.extras
        setupToolbar(extras?.getString(Constant.PARAM_GENRE_NAME), true)
        viewModel = ViewModelProvider(this).get(MovieListViewModel::class.java)
        extras?.getInt(Constant.PARAM_GENRE_ID)?.let {
            viewModel.setGenreId(it)
        }
        setupList()
        setupListener()
        observeData()
    }

    private fun setupList() {
        movieAdapter = MovieAdapter(this)
        movieAdapter?.itemEventListener = this
        binding.rvMovie.apply {
            val context = this@MovieListActivity
            layoutManager = LinearLayoutManager(context)
            adapter = movieAdapter
            addItemDecoration(SpacedItemDecoration(context))
        }
    }

    private fun setupListener() {
        binding.layoutLoadingError.btnRetry.setOnClickListener {
            viewModel.retry()
        }
    }

    private fun observeData() {
        viewModel.livePagedList.observe(this, Observer {
            movieAdapter?.submitList(it)
        })
        viewModel.liveRequestState.observe(this, Observer {
            when {
                it.isLoading() -> {
                    if (it.requestPage == 1) binding.isLoading = true
                }
                it.isSuccess() -> {
                    binding.isLoading = false
                    binding.isError = false
                }
                it.isLoadEmpty() -> {
                    if (it.requestPage == 1) {
                        binding.isLoading = false
                        binding.isError = true
                        binding.message = "Tidak ditemukan film di kategori ini"
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

    override fun onClickMovie(data: Movie) {
        val intent = Intent(this, MovieDetailActivity::class.java)
        intent.putExtra(Constant.PARAM_MOVIE_ID, data.id)
        intent.putExtra(Constant.PARAM_MOVIE_NAME, data.title)
        startActivity(intent)
    }
}
