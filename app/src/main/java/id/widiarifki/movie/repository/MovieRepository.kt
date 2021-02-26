package id.widiarifki.movie.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import id.widiarifki.movie.data.network.APIService
import id.widiarifki.movie.data.model.Movie
import id.widiarifki.movie.data.model.MovieStatus
import id.widiarifki.movie.data.model.Video
import id.widiarifki.movie.repository.pagingsource.MoviePagingSource
import id.widiarifki.movie.repository.pagingsource.WatchlistPagingSource
import id.widiarifki.movie.utils.livedata.StatedData
import id.widiarifki.movie.utils.livedata.StatedLiveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MovieRepository
@Inject constructor(
    private val apiService: APIService
) {

    companion object {
        private const val NETWORK_PAGE_SIZE = 10
    }

    private suspend fun getVideos(movieId: Int): StatedData<List<Video>> {
        try {
            val request = apiService.getMovieVideos(movieId)
            if (request.success) {
                return StatedData.load(request.results)
            } else {
                return StatedData.error(request.status_message)
            }
        } catch (e: Exception) {
            return StatedData.error(e.message)
        }
    }

    fun getPagingMovies(genreId: Int?): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = NETWORK_PAGE_SIZE),
            pagingSourceFactory = { MoviePagingSource(apiService, genreId) }
        ).flow
    }

    fun getPagingWatchlist(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = NETWORK_PAGE_SIZE),
            pagingSourceFactory = { WatchlistPagingSource(apiService) }
        ).flow
    }

    suspend fun getLiveDetail(id: Int): StatedLiveData<Movie> {
        val liveData: StatedLiveData<Movie> = StatedLiveData()
        try {
            val request = apiService.getMovieDetail(id)
            if (request.isSuccessful) {
                liveData.postValue(StatedData.load(request.body()))
            } else {
                liveData.postValue(StatedData.error(request.message()))
            }
        } catch (e: Exception) {
            liveData.postValue(StatedData.error(e.message))
        }
        return liveData
    }

    suspend fun getLiveTrailer(movieId: Int): StatedLiveData<Video> {
        val liveData: StatedLiveData<Video> = StatedLiveData()
        val videos = getVideos(movieId)
        videos.data?.firstOrNull {
            it.site.equals("YouTube", true)
                    && it.type.equals("Trailer", true)
        }?.let {
            liveData.postValue(StatedData.load(it))
        } ?: kotlin.run {
            liveData.postValue(StatedData.error(videos.getMessage()))
        }
        return liveData
    }

    suspend fun getLiveStatus(movieId: Int): LiveData<StatedData<MovieStatus>> {
        val liveData: MutableLiveData<StatedData<MovieStatus>> = MutableLiveData()
        try {
            val request = apiService.getMovieStatus(movieId)
            if (request.success) {
                liveData.postValue(StatedData.load(request.data))
            } else {
                liveData.postValue(StatedData.error(request.status_message))
            }
        } catch (e: Exception) {
            liveData.postValue(StatedData.error(e.message))
        }
        return liveData
    }

    /*suspend fun getDetail(id: Int): StatedData<Movie> {
        try {
            val request = apiService.getMovieDetail(id)
            if (request.isSuccessful) {
                return StatedData.success(request.body())
            } else {
                return StatedData.error(request.message())
            }
        } catch (e: Exception) {
            return StatedData.error(e.message)
        }
    }*/

    suspend fun addToWatchlist(movieId: Int?): StatedData<Boolean> {
        try {
            val request = apiService.addToWatchlist(mediaId = movieId)
            if (request.success) {
                return StatedData.success()
            } else {
                return StatedData.error(request.status_message)
            }
        } catch (e: Exception) {
            return StatedData.error(e.message)
        }
    }

}
