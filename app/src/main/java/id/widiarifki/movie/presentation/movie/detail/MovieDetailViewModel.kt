package id.widiarifki.movie.presentation.movie.detail

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import id.widiarifki.movie.data.model.Movie
import id.widiarifki.movie.data.model.MovieStatus
import id.widiarifki.movie.repository.MovieRepository
import id.widiarifki.movie.utils.ParamConstant
import id.widiarifki.movie.utils.livedata.StatedLiveData
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel
@Inject constructor(
    private val repository: MovieRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _movieId: Int = savedStateHandle.get<Int>(ParamConstant.MOVIE_ID) ?: 0

    val movieDetail: StatedLiveData<Movie> = fetchMovieDetail()
    val watchlistStatus: StatedLiveData<Boolean?> = fetchWatchlistStatus()

    /**
     * getMovieDetail() blocks will contains chaining request to 3 API endpoints
     * the chaining request will take advantage of MediatorLiveData class
     *
     * #1, get detail of movie. @return StatedData<Movie>
     * #2, get youtube key. @return String
     * /** #3, get movie status (watchlist/favorite/etc). @return MovieStatus **/
     */
    private fun fetchMovieDetail() : StatedLiveData<Movie> {
        // StatedLiveData deferred from MediatorLiveData which able to hold the request together
        val mediatorLiveData = StatedLiveData<Movie>()
        // movie will hold the updated data that will be used by activity
        var movie: Movie? = null

        try {

            viewModelScope.launch {

                // #1 GET MOVIE DETAIL
                mediatorLiveData.addSource(repository.getMovieDetail(_movieId)) {
                    when {
                        it.isSuccess() -> {
                            movie = it?.data
                            mediatorLiveData.load(movie)

                            viewModelScope.launch {

                                // #2 GET YOUTUBE TRAILER VIDEO
                                mediatorLiveData.addSource(getYtTrailerKey()) {
                                    mediatorLiveData.load(movie?.apply {
                                        ytTrailerKey = it
                                    })

                                    /*viewModelScope.launch {

                                        // #3 GET WATCHLIST STATUS
                                        mediatorLiveData.addSource(getMovieStatus()) {
                                            mediatorLiveData.load(movie?.apply {
                                                isWatchlist = it?.watchlist
                                            })
                                        }

                                    }*/
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

    private suspend fun getYtTrailerKey(): LiveData<String?> = Transformations.map(
            repository.getYtTrailer(_movieId)
    ) {
        it.data?.key
    }

    private suspend fun getMovieStatus(): LiveData<MovieStatus?> = Transformations.map(
        repository.getMovieStatus(_movieId)
    ) {
        it.data
    }

    private fun fetchWatchlistStatus(): StatedLiveData<Boolean?> {
        // todo: ini bisa lebih simple lg sih..
        val liveData = StatedLiveData<Boolean?>()
        viewModelScope.launch {
            liveData.addSource(repository.getMovieStatus(_movieId)) {
                when {
                    it.isSuccess() -> liveData.load(it.data?.watchlist)
                    it.isError() -> liveData.error(it.message)
                }
            }
        }
        return liveData
    }

    fun updateWatchlist(updateValue: Boolean) {
        watchlistStatus.loading()
        viewModelScope.launch {
            watchlistStatus.addSource(repository.updateWatchlist(_movieId, updateValue)) {
                when {
                    it.isLoading() -> watchlistStatus.loading()
                    it.isError() -> watchlistStatus.error(it.message)
                    it.isSuccess() -> watchlistStatus.load(updateValue)
                }
            }
        }
    }
}