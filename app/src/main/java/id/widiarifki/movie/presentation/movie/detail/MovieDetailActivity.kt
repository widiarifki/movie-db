package id.widiarifki.movie.presentation.movie.detail

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import dagger.hilt.android.AndroidEntryPoint
import id.widiarifki.movie.R
import id.widiarifki.movie.base.BaseActivity
import id.widiarifki.movie.data.model.Movie
import id.widiarifki.movie.databinding.ActivityMovieDetailBinding
import id.widiarifki.movie.presentation.review.ReviewActivity
import id.widiarifki.movie.utils.Constant
import id.widiarifki.movie.utils.livedata.StatedData

@AndroidEntryPoint
class MovieDetailActivity : BaseActivity<ActivityMovieDetailBinding>() {

    private val viewModel: MovieDetailViewModel by viewModels()
    private var movieId: Int? = null
    private var movieName: String? = null
    private var mYoutubePlayer: YouTubePlayer? = null
    private var actionBarMenu: Menu? = null

    override val resourceLayout: Int
        get() = R.layout.activity_movie_detail

    override fun onViewReady(savedInstanceState: Bundle?) {
        handleIntent()
        setupToolbar(movieName, true)
        setupListener()
        observeData()
    }

    private fun handleIntent() {
        val extras = intent.extras
        if (extras != null) {
            movieId = extras.getInt(Constant.PARAM_MOVIE_ID)
            movieName = extras.getString(Constant.PARAM_MOVIE_NAME)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        actionBarMenu = menu
        menuInflater.inflate(R.menu.menu_movie_detail, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_watchlist_toggle -> addToWatchlist()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun addToWatchlist() {
        viewModel.addToWatchlist(movieId).observe(this, {
            when {
                it.isLoading() -> showProgressDialog("Menambahkan ke watchlist..")
                it.isSuccess() -> {
                    hideProgressDialog()
                    showToast("Berhasil menambahkan ke watchlist")
                }
                it.isError() -> {
                    hideProgressDialog()
                    showToast(it.message)
                }
            }
        })
    }

    private fun setupListener() {
        //TODO: coba pindahin as data binding
        binding.layoutLoadingError.btnRetry.setOnClickListener {
            observeData()
        }
    }

    private fun observeData() {
        movieId?.let {
            viewModel.getAllDetail(it).observe(this, {
                onDetailLoaded(it)
            })
        } ?: kotlin.run {
            finish()
        }
    }

    private fun onDetailLoaded(data: StatedData<Movie>?) {
        data?.let {
            binding.isLoading = it.isLoading()
            if (!it.isLoading()) {
                binding.isError = it.isError()
                binding.message = if (it.isError()) it.message else null

                // After data loaded
                it.data?.let { data ->
                    binding.data = data

                    // Trailer
                    if (binding.isTrailerReady != true) {
                        // Condition only when trailer not defined as ready (prevent redundant)
                        binding.isTrailerLoading = true
                        data.ytTrailerKey?.let { videoId ->
                            setupPlayer(videoId)
                        } ?: run {
                            binding.isTrailerLoading = false
                        }
                    }

                    // Movie status
                    toggleWatchlistFlag(data.isWatchlist)
                }
            }
        }
    }

    private fun toggleWatchlistFlag(isWatchlist: Boolean?) {
        val icon = if (isWatchlist == true) R.drawable.ic_action_bookmark_on else R.drawable.ic_action_bookmark_off
        actionBarMenu?.findItem(R.id.menu_watchlist_toggle)
                ?.setIcon(icon)
    }

    private fun setupPlayer(videoId: String) {
        binding.videoPlayer.apply {
            lifecycle.addObserver(this)
            addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    super.onReady(youTubePlayer)
                    youTubePlayer.cueVideo(videoId, 0f)
                    binding.isTrailerLoading = false
                    binding.isTrailerReady = true
                    mYoutubePlayer = youTubePlayer
                }

                override fun onStateChange(
                        youTubePlayer: YouTubePlayer,
                        state: PlayerConstants.PlayerState
                ) {
                    super.onStateChange(youTubePlayer, state)
                    when (state) {
                        PlayerConstants.PlayerState.BUFFERING -> binding.isTrailerPlaying = true
                        PlayerConstants.PlayerState.PLAYING -> binding.isTrailerPlaying = true
                        PlayerConstants.PlayerState.PAUSED -> binding.isTrailerPlaying = true
                        PlayerConstants.PlayerState.UNSTARTED -> binding.isTrailerPlaying = true
                        PlayerConstants.PlayerState.VIDEO_CUED -> binding.isTrailerPlaying = false
                        PlayerConstants.PlayerState.ENDED -> binding.isTrailerPlaying = false
                        PlayerConstants.PlayerState.UNKNOWN -> binding.isTrailerPlaying = true
                    }
                }

                override fun onError(
                        youTubePlayer: YouTubePlayer,
                        error: PlayerConstants.PlayerError
                ) {
                    super.onError(youTubePlayer, error)
                    binding.isTrailerLoading = false
                    binding.isTrailerPlaying = false
                    showToast(error.name)
                }
            })
        }
    }

    fun playVideoTrailer(view: View) {
        if (binding.isTrailerReady == true) {
            binding.isTrailerPlaying = true
            mYoutubePlayer?.play()
        } else {
            view.visibility = View.GONE
        }
    }

    fun showReview(view: View) {
        val intent = Intent(this, ReviewActivity::class.java)
        intent.putExtra(Constant.PARAM_MOVIE_ID, movieId)
        intent.putExtra(Constant.PARAM_MOVIE_NAME, movieName)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.videoPlayer.release()
    }
}
