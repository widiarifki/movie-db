package id.widiarifki.movie.repository

import androidx.lifecycle.LiveData
import id.widiarifki.movie.BaseApplication
import id.widiarifki.movie.base.repository.APIRepository
import id.widiarifki.movie.base.repository.LocalRepository
import id.widiarifki.movie.data.api.APIService
import id.widiarifki.movie.data.dao.GenreDao
import id.widiarifki.movie.data.model.Genre
import id.widiarifki.movie.helper.StatedData
import javax.inject.Inject

class GenreRepository(private val dao: GenreDao) : APIRepository<Genre>, LocalRepository<Genre> {

    @Inject
    lateinit var apiService: APIService

    init {
        BaseApplication.applicationComponent.inject(this)
    }

    override val cache: LiveData<List<Genre>?>
        get() = dao.getAllGenre()

    override suspend fun updateAllCache(data: List<Genre>) {
        dao.deleteAll()
        dao.insert(data)
    }

    override suspend fun refreshData() {
        try {
            val request = apiService.getGenre()
            if (request.results?.isNotEmpty() == true) {
                updateAllCache(request.results!!)
            }
        } catch (e: Exception) {
            throw Exception(e)
        }
    }

    override suspend fun getList(): StatedData<List<Genre>?> {
        TODO("Not yet implemented")
    }

    override suspend fun getDetail(id: Int): StatedData<Genre> {
        TODO("Not yet implemented")
    }

}