package id.widiarifki.movie.repository

import id.widiarifki.movie.BaseApplication
import id.widiarifki.movie.base.repository.APIRepository
import id.widiarifki.movie.data.api.APIService
import id.widiarifki.movie.data.model.Movie
import id.widiarifki.movie.data.model.Video
import id.widiarifki.movie.helper.StatedData
import javax.inject.Inject

class MovieRepository : APIRepository<Movie> {

    @Inject
    lateinit var apiService: APIService

    init {
        BaseApplication.applicationComponent.inject(this)
    }

    override suspend fun getDetail(id: Int): StatedData<Movie> {
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

    override suspend fun refreshData() {
        TODO("Not yet implemented")
    }

    override suspend fun getList(): StatedData<List<Movie>?> {
        TODO("Not yet implemented")
    }

}
