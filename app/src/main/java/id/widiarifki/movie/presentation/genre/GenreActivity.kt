package id.widiarifki.movie.presentation.genre

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import id.widiarifki.movie.R
import id.widiarifki.movie.base.BaseActivity
import id.widiarifki.movie.data.model.Genre
import id.widiarifki.movie.databinding.ActivityGenreBinding
import id.widiarifki.movie.presentation.movie.list.MovieListActivity
import id.widiarifki.movie.utils.Constant
import id.widiarifki.movie.utils.ui.SpacedItemDecoration

@AndroidEntryPoint
class GenreActivity : BaseActivity<ActivityGenreBinding>(),
    GenreAdapter.ItemGenreListener {

    private val viewModel: GenreViewModel by viewModels()
    private var genres: ArrayList<Genre> = ArrayList()
    private var genreAdapter: GenreAdapter? = null

    override val resourceLayout: Int = R.layout.activity_genre

    override fun onViewReady(savedInstanceState: Bundle?) {
        setupList()
        setupListener()
        observeData()
    }

    private fun setupList() {
        genreAdapter = GenreAdapter(this, genres)
        binding.rvGenre.apply {
            adapter = genreAdapter
            layoutManager = GridLayoutManager(this@GenreActivity, 2, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(SpacedItemDecoration(this@GenreActivity))
        }
    }

    private fun setupListener() {
        genreAdapter?.itemListener = this
        binding.layoutLoadingError.btnRetry.setOnClickListener {
            viewModel.refreshGenre()
        }
    }

    private fun observeData() {
        viewModel.genresLiveData.observe(this, {
            binding.isLoading = it.isLoading()
            binding.isError = it.isFail() || it.data.isNullOrEmpty()
            if (it.data?.isNotEmpty() == true){
                setAdapterData(it.data)
                if (it.isFail()) {
                    showToast(it.message)
                }
            } else {
                if (it.isFail()) {
                    binding.message = it.message
                } else {
                    binding.message = getString(R.string.msg_empty_category)
                }
            }
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
        intent.putExtra(Constant.PARAM_GENRE_ID, data.id)
        intent.putExtra(Constant.PARAM_GENRE_NAME, data.name)
        startActivity(intent)
    }

}