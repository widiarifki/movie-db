package id.widiarifki.movie.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import id.widiarifki.movie.data.network.APIService
import id.widiarifki.movie.data.model.Movie
import id.widiarifki.movie.data.model.Video
import id.widiarifki.movie.data.source.MoviePagingSource
import id.widiarifki.movie.utils.StatedData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieRepository
@Inject constructor(
    private val apiService: APIService
) {

    companion object {
        private const val NETWORK_PAGE_SIZE = 10
    }

    fun getMoviesByGenre(genreId: Int?): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = NETWORK_PAGE_SIZE),
            pagingSourceFactory = { MoviePagingSource(apiService, genreId) }
        ).flow
    }

    suspend fun getDetail(id: Int): StatedData<Movie> {
        try {
            val data = StatedData<Movie>()
            val request = apiService.getMovieDetail(id)

            if (request.isSuccessful) {
                data.data = request.body()
            } else {
                data.message = request.message()
            }

            return data
        } catch (e: Exception) {
            throw Exception(e)
        }
    }

    suspend fun getVideos(movieId: Int): StatedData<List<Video>> {
        try {
            val data = StatedData<List<Video>>()
            val request = apiService.getMovieVideos(movieId)

            if (request.success) {
                data.data = request.results
            } else {
                data.message = request.status_message.toString()
            }

            return data
        } catch (e: Exception) {
            throw Exception(e)
        }
    }

}
