package id.widiarifki.movie.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import id.widiarifki.movie.data.network.APIService
import id.widiarifki.movie.data.model.Review
import id.widiarifki.movie.data.source.ReviewPagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReviewRepository
@Inject constructor(
    private val apiService: APIService
) {

    companion object {
        private const val NETWORK_PAGE_SIZE = 10
    }

    fun getReviews(movieId: Int?): Flow<PagingData<Review>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = NETWORK_PAGE_SIZE),
            pagingSourceFactory = { ReviewPagingSource(apiService, movieId) }
        ).flow
    }
}
