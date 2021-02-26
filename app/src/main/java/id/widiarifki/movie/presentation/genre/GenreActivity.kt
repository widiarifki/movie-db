package id.widiarifki.movie.presentation.genre

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import id.widiarifki.movie.R
import id.widiarifki.movie.base.BaseActivity
import id.widiarifki.movie.data.model.Genre
import id.widiarifki.movie.databinding.ActivityGenreBinding
import id.widiarifki.movie.presentation.movie.list.MovieListActivity
import id.widiarifki.movie.presentation.watchlist.WatchlistActivity
import id.widiarifki.movie.utils.ParamConstant
import id.widiarifki.movie.utils.ui.SpacedItemDecoration

@AndroidEntryPoint
class GenreActivity : BaseActivity<ActivityGenreBinding>(),
    GenreAdapter.ItemGenreListener {

    private val viewModel: GenreViewModel by viewModels()
    private var genres: ArrayList<Genre> = ArrayList()
    private var genreAdapter: GenreAdapter = GenreAdapter(genres)

    override val resourceLayout: Int = R.layout.activity_genre

    override fun onViewReady(savedInstanceState: Bundle?) {
        setupList()
        setupListener()
        observeData()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_watchlist -> gotoWatchlist()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun gotoWatchlist() {
        val intent = Intent(this, WatchlistActivity::class.java)
        startActivity(intent)
    }

    private fun setupList() {
        binding.rvGenre.apply {
            adapter = genreAdapter
            layoutManager = GridLayoutManager(this@GenreActivity, 2, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(SpacedItemDecoration(this@GenreActivity))
        }
    }

    private fun setupListener() {
        genreAdapter.itemListener = this
        binding.layoutLoadingError.btnRetry.setOnClickListener {
            viewModel.refreshGenre()
        }
    }

    private fun observeData() {
        viewModel.liveGenres.observe(this, {
            when {
                it.isLoading() -> {
                    binding.isLoading = true && it.data.isNullOrEmpty() && genres.isEmpty()
                }
                it.isError() -> {
                    binding.isLoading = false
                    binding.isError = true
                    if (genres.isNotEmpty()) {
                        showToast(binding.message)
                    }
                }
                it.isSuccess() -> {
                    binding.isLoading = false
                    if (it.data.isNullOrEmpty() && genres.isEmpty()) {
                        binding.isError = true
                        binding.message = getString(R.string.msg_empty_category)
                    } else {
                        setAdapterData(it.data)
                    }
                }
            }

            /*binding.isLoading = it.isLoading() && it.data.isNullOrEmpty()
            binding.isError = it.isError() || it.data.isNullOrEmpty()
            if (it.data?.isNotEmpty() == true) {
                setAdapterData(it.data)
                if (it.isError()) {
                    showToast(it.message)
                }
            } else {
                if (it.isError()) {
                    binding.message = it.message
                } else {
                    binding.message = getString(R.string.msg_empty_category)
                }
            }*/
        })
    }

    private fun setAdapterData(data: List<Genre>?) {
        if(data?.isNotEmpty() == true) {
            genres.clear()
            genres.addAll(data)
            genreAdapter?.notifyDataSetChanged()
        }
    }

    override fun onClickGenre(data: Genre) {
        val intent = Intent(this, MovieListActivity::class.java)
        intent.putExtra(ParamConstant.PARAM_GENRE_ID, data.id)
        intent.putExtra(ParamConstant.PARAM_GENRE_NAME, data.name)
        startActivity(intent)
    }

}