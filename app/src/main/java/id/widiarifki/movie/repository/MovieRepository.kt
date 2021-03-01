package id.widiarifki.movie.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingState
import id.widiarifki.movie.data.network.APIService
import id.widiarifki.movie.data.model.Movie
import id.widiarifki.movie.data.model.MovieStatus
import id.widiarifki.movie.data.model.Video
import id.widiarifki.movie.repository.pagingsource.MoviePagingSource
import id.widiarifki.movie.repository.pagingsource.WatchlistPagingSource
import id.widiarifki.movie.utils.livedata.StatedData
import id.widiarifki.movie.utils.livedata.StatedLiveData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class MovieRepository
@Inject constructor(
    private val apiService: APIService
) {

    companion object {
        private const val NETWORK_PAGE_SIZE = 10
    }

    private suspend fun getVideos(movieId: Int): StatedData<List<Video>> {
        return try {
            val response = apiService.getMovieVideos(movieId)
            StatedData.load(response.results)
        } catch (e: Exception) {
            StatedData.error(e.message)
        }
    }

    fun getPagingMovies(genreId: Int?): Flow<PagingData<Movie>> {
        val pagingSource = MoviePagingSource(apiService, genreId)
        val pager = Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = NETWORK_PAGE_SIZE),
            pagingSourceFactory = { pagingSource }
        )
        return pager
            .flow
            .onEach {

                Log.v("CURRENTPAGE", pagingSource.currentPage.toString())
                if (pagingSource.currentPage == pagingSource.defaultStartPage) {
                    //Log.v("CURRENTPAGE", pagingSource.currentPage.toString())
                }
            }
            .onEmpty {
                if (pagingSource.currentPage == pagingSource.defaultStartPage) {
                    // todo: load dari cache aja
                }
            }
            .catch {
                // todo: load dari cache aja
            }
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
            val movie = apiService.getMovieDetail(id)
            liveData.postValue(StatedData.load(movie))
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
            liveData.postValue(StatedData.error(videos.message))
        }
        return liveData
    }

    suspend fun getLiveStatus(movieId: Int): StatedLiveData<MovieStatus> {
        val liveData: StatedLiveData<MovieStatus> = StatedLiveData()
        try {
            val movieStatus = apiService.getMovieStatus(movieId)
            liveData.load(movieStatus)
        } catch (e: Exception) {
            liveData.error(e.message)
        }
        return liveData
    }

    suspend fun updateWatchlist(movieId: Int, updateValue: Boolean): Boolean? {
        try {
            val request = apiService.updateWatchlist(mediaId = movieId, watchList = updateValue)
            return request.success
        } catch (e: Exception) {
            throw (e)
        }
    }

}
