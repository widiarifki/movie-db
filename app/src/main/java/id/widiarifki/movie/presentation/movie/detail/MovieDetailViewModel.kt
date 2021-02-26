package id.widiarifki.movie.presentation.movie.detail

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import id.widiarifki.movie.data.model.Movie
import id.widiarifki.movie.data.model.MovieStatus
import id.widiarifki.movie.repository.MovieRepository
import id.widiarifki.movie.utils.livedata.StatedLiveData
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel
@Inject constructor(
    private val repository: MovieRepository
): ViewModel() {

    fun getAllDetail(movieId: Int) : StatedLiveData<Movie> {
        val mediatorLiveData = StatedLiveData<Movie>()
        var movie: Movie? = null

        try {
            /**
             * Chaining request
             * #1 Request, get detail of movie. @return StatedData<Movie>
             * #2 Request, get youtube key. @return String
             * #3 Request, get movie status (watchlist/favorite/etc). @return MovieStatus
             */

            // #1
            viewModelScope.launch {
                mediatorLiveData.addSource(repository.getLiveDetail(movieId)) {
                    when {
                        it.isSuccess() -> {
                            movie = it?.data
                            mediatorLiveData.load(movie)

                            // #2
                            viewModelScope.launch {
                                mediatorLiveData.addSource(getYtTrailerKey(movieId)) {
                                    movie?.ytTrailerKey = it
                                    mediatorLiveData.load(movie)

                                    // #3
                                    viewModelScope.launch {
                                        mediatorLiveData.addSource(getMovieStatus(movieId)) {
                                            movie?.isWatchlist = it?.watchlist
                                            mediatorLiveData.load(movie)
                                        }
                                    }
                                }
                            }
                        }
                        it.isError() -> {
                            mediatorLiveData.error(it.message)
                        }
                        else -> {
                            mediatorLiveData.loading()
                        }
                    }
                }
            }
        } catch (e: Exception) {
            mediatorLiveData.error(e.message)
        }

        return mediatorLiveData
    }

    private suspend fun getYtTrailerKey(movieId: Int): LiveData<String?> = Transformations.map(
            repository.getLiveTrailer(movieId)
    ) {
        it.data?.key
    }

    private suspend fun getMovieStatus(movieId: Int): LiveData<MovieStatus?> = Transformations.map(repository.getLiveStatus(movieId)) {
        it.data
    }

    fun addToWatchlist(movieId: Int?): StatedLiveData<Boolean> {
        val liveData: StatedLiveData<Boolean> = StatedLiveData()
        viewModelScope.launch {
            val result = repository.addToWatchlist(movieId)
            if (result.isSuccess()) {
                liveData.success()
            } else {
                liveData.error(result.message)
            }
        }
        return liveData
    }
}