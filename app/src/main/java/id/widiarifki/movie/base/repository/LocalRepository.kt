package id.widiarifki.movie.base.repository

import androidx.lifecycle.LiveData

interface LocalRepository<Data> {

    val cache: LiveData<List<Data>?>
    suspend fun updateAllCache(data: List<Data>)

}