package id.widiarifki.movie.presentation.genre

import android.content.Intent
import android.os.Bundle
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
import kotlinx.android.synthetic.main.activity_genre.*

@AndroidEntryPoint
class GenreActivity : BaseActivity<ActivityGenreBinding>(),
    GenreAdapter.ItemGenreListener {

    private val genreViewModel: GenreViewModel by viewModels()
    private var genres: ArrayList<Genre> = ArrayList()

    override val resourceLayout: Int = R.layout.activity_genre

    override fun onViewReady(savedInstanceState: Bundle?) {
        binding.apply {
            viewModel = genreViewModel
            lifecycleOwner = this@GenreActivity
        }

        setupListView()
        setupListener()
        observeData()
    }

    private fun setupListener() {
        refreshLayout.setOnRefreshListener {
            genreViewModel.retry()
        }
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

    private fun setupListView() {
        binding.rvGenre.apply {
            adapter = GenreAdapter(genres).withListener(this@GenreActivity)
            layoutManager = GridLayoutManager(this@GenreActivity, 2, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(SpacedItemDecoration(this@GenreActivity))
        }
    }

    private fun observeData() {
        genreViewModel.genres.observe(this, {
            refreshLayout.isRefreshing = it.isLoading()
            when {
                it.isSuccess() -> {
                    updateGenres(it.data)
                }
            }
        })
    }

    private fun updateGenres(data: List<Genre>?) {
        if(data?.isNotEmpty() == true) {
            genres.clear()
            genres.addAll(data)
        }
    }

    override fun onClickGenre(data: Genre) {
        val intent = Intent(this, MovieListActivity::class.java)
        intent.putExtra(ParamConstant.GENRE_ID, data.id)
        intent.putExtra(ParamConstant.GENRE_NAME, data.name)
        startActivity(intent)
    }

}