package id.widiarifki.movie.feature.movie.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import id.widiarifki.movie.data.model.Movie
import id.widiarifki.movie.data.model.Video
import id.widiarifki.movie.repository.MovieRepository
import id.widiarifki.movie.helper.StatedLiveData
import kotlinx.coroutines.launch

class MovieDetailViewModel(application: Application): AndroidViewModel(application) {

    private val repository = MovieRepository()

    fun getMovie(movieId: Int?) : StatedLiveData<Movie> {
        val liveData = StatedLiveData<Movie>()
        liveData.loading()

        viewModelScope.launch {
            try {
                movieId?.let {
                    val stateMovie = repository.getDetail(it)
                    if (stateMovie.data != null)
                        liveData.loaded(stateMovie.data)
                    else
                        liveData.fail(stateMovie.message)
                } ?: run {
                    liveData.fail("ID tidak diketahui")
                }

            } catch (e: Exception) {
                liveData.fail(e.message)
            }
        }

        return liveData
    }

    fun getTrailer(movieId: Int?) : StatedLiveData<Video> {
        val liveData = StatedLiveData<Video>()
        liveData.loading()

        viewModelScope.launch {
            try {
                movieId?.let {
                    val stateVideos = repository.getVideos(it)
                    val trailerVideo = stateVideos.data?.firstOrNull {
                        it.site.equals("YouTube", true) && it.type.equals("Trailer", true)
                    }
                    liveData.loaded(trailerVideo)
                } ?: run {
                    liveData.fail("ID tidak diketahui")
                }
            } catch (e: Exception) {
                liveData.fail(e.message)
            }
        }

        return liveData
    }
}