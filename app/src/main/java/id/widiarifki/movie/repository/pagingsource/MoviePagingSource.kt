package id.widiarifki.movie.repository.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import id.widiarifki.movie.data.network.APIService
import id.widiarifki.movie.data.model.Movie
import retrofit2.HttpException
import java.io.IOException

/**
 * Deferred from PagingSource<Key, Value>
 * PagingSource takes two parameters, a key and a value.
 * Key defines what data to load. E.g. Int as a page number or String as a next page token.
 * Value defines the type of data that will be loaded
 */
class MoviePagingSource(
    private val apiService: APIService,
    private val genreId: Int?
): PagingSource<Int, Movie>() {

    val defaultStartPage = 1
    var currentPage = defaultStartPage

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val page = params.key ?: 1
        try {
            val response = apiService.getMovieByGenre(genreId, page)
            val movies = response.results.orEmpty()
            currentPage = page
            return LoadResult.Page(
                data = movies,
                prevKey = if (page == defaultStartPage) null else page-1,
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

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

}