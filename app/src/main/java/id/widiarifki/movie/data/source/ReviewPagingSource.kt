package id.widiarifki.movie.data.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import id.widiarifki.movie.data.network.APIService
import id.widiarifki.movie.data.model.Review
import retrofit2.HttpException
import java.io.IOException

class ReviewPagingSource(
    private val apiService: APIService,
    private val movieId: Int?
) : PagingSource<Int, Review>() {

    private val DEFAULT_STARTING_PAGE = 1

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Review> {
        val page = params.key ?: 1
        try {
            val response = apiService.getMovieReviews(movieId, page)
            val movies = response.results.orEmpty()
            return LoadResult.Page(
                data = movies,
                prevKey = if (page == DEFAULT_STARTING_PAGE) null else page-1,
                nextKey = if (movies.isEmpty()) null else page+1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Review>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

}
