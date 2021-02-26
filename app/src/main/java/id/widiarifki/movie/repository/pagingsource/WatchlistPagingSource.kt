package id.widiarifki.movie.repository.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import id.widiarifki.movie.data.model.Movie
import id.widiarifki.movie.data.network.APIService

class WatchlistPagingSource(
    private val apiService: APIService
) : PagingSource<Int, Movie>() {

    companion object {
        const val DEFAULT_STARTING_PAGE = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val page = params.key ?: DEFAULT_STARTING_PAGE
        return try {
            val response = apiService.getWatchlist(page = page)
            val movies = response.results.orEmpty()
            LoadResult.Page(
                data = movies,
                prevKey = if (page == DEFAULT_STARTING_PAGE) null else page-1,
                nextKey = if (movies.isNullOrEmpty()) null else page+1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

}
