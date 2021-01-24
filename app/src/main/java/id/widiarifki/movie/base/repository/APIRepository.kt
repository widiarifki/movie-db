package id.widiarifki.movie.base.repository

import id.widiarifki.movie.helper.StatedData

interface APIRepository<Data> {

    suspend fun refreshData()
    suspend fun getList() : StatedData<List<Data>?>
    suspend fun getDetail(id: Int) : StatedData<Data>
}