package id.widiarifki.movie.feature.genre

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import id.widiarifki.movie.R
import id.widiarifki.movie.base.BaseActivity
import id.widiarifki.movie.data.model.Genre
import id.widiarifki.movie.databinding.ActivityGenreBinding
import id.widiarifki.movie.feature.movie.list.MovieListActivity
import id.widiarifki.movie.helper.Constant
import id.widiarifki.movie.helper.ui.SpacedItemDecoration

class GenreActivity : BaseActivity<ActivityGenreBinding>(), GenreAdapter.ItemGenreListener {

    private lateinit var viewModel: GenreViewModel
    private var genreList: ArrayList<Genre> = ArrayList()
    private var genreAdapter: GenreAdapter? = null

    override val resourceLayout: Int
        get() = R.layout.activity_genre

    override fun onViewReady(savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this).get(GenreViewModel::class.java)
        setupList()
        setupListener()
        observeData()
    }

    private fun setupList() {
        genreAdapter = GenreAdapter(this, genreList)
        genreAdapter?.itemEventListener = this
        binding.rvGenre.apply {
            adapter = genreAdapter
            layoutManager = GridLayoutManager(this@GenreActivity, 2, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(SpacedItemDecoration(this@GenreActivity))
        }
    }

    private fun setupListener() {
        binding.layoutLoadingError.btnRetry.setOnClickListener {
            viewModel.refreshGenre()
        }
    }

    private fun observeData() {
        viewModel.liveGenre.observe(this, Observer {
            when {
                it.isLoading() -> binding.isLoading = true
                it.isSuccess() -> {
                    binding.isLoading = false
                    if (it.data.isNullOrEmpty()){
                        binding.isError = true
                        binding.message = "Tidak menemukan kategori"
                    } else {
                        binding.isError = false
                        setAdapterData(it.data)
                    }
                }
                it.isFail() -> {
                    binding.isLoading = false
                    if (it.data.isNullOrEmpty()) {
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

    private fun setAdapterData(data: List<Genre>?) {
        if(data?.isNotEmpty() == true) {
            genreList.clear()
            genreList.addAll(data)
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