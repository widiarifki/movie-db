package id.widiarifki.movie.repository

import androidx.lifecycle.LiveData
import id.widiarifki.movie.data.network.APIService
import id.widiarifki.movie.data.local.dao.GenreDao
import id.widiarifki.movie.data.model.Genre
import id.widiarifki.movie.utils.livedata.StatedLiveData
import javax.inject.Inject

class GenreRepository
@Inject constructor(
    private val apiService: APIService,
    private val genreDao: GenreDao
) {

    val cache: LiveData<List<Genre>> get() = genreDao.getAll()

    suspend fun fetchGenreList(): StatedLiveData<List<Genre>> {
        val liveData = StatedLiveData<List<Genre>>()
        try {
            val request = apiService.getGenre()
            liveData.load(request.results)

            updateCache(request.results)
        } catch (e: Exception) {
            liveData.error(e)
        }
        return liveData
    }

    private suspend fun updateCache(data: List<Genre>?) {
        if (!data.isNullOrEmpty()) {
            genreDao.deleteAll()
            genreDao.insert(data)
        }
    }

}