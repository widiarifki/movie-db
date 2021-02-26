package id.widiarifki.movie.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import id.widiarifki.movie.data.model.Movie
import id.widiarifki.movie.data.network.APIService
import id.widiarifki.movie.repository.pagingsource.WatchlistPagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WatchlistRepository
@Inject constructor(
    private val apiService: APIService
){

    companion object {
        private const val NETWORK_PAGE_SIZE = 10
    }

    fun getPagingWatchlist() : Flow<PagingData<Movie>> {
        return Pager(
            pagingSourceFactory = { WatchlistPagingSource(apiService) },
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE)
        ).flow
    }
}