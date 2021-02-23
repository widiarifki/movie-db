package id.widiarifki.movie.repository

import androidx.lifecycle.LiveData
import id.widiarifki.movie.data.network.APIService
import id.widiarifki.movie.data.dao.GenreDao
import id.widiarifki.movie.data.model.Genre
import javax.inject.Inject

class GenreRepository
@Inject constructor(
    private val apiService: APIService,
    private val genreDao: GenreDao
) {

    val cache: LiveData<List<Genre>?> get() = genreDao.getAllGenre()

    suspend fun updateAllCache(data: List<Genre>) {
        genreDao.deleteAll()
        genreDao.insert(data)
    }

    suspend fun refreshData() {
        try {
            val request = apiService.getGenre()
            if (request.results?.isNotEmpty() == true) {
                updateAllCache(request.results!!)
            }
        } catch (e: Exception) {
            throw Exception(e)
        }
    }

}