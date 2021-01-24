package id.widiarifki.movie.repository.pagedsource

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import id.widiarifki.movie.BaseApplication
import id.widiarifki.movie.data.api.APIService
import id.widiarifki.movie.data.model.Movie
import id.widiarifki.movie.helper.State
import id.widiarifki.movie.repository.MovieRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class MoviePagedDataSource(val scope: CoroutineScope, val params: Bundle? = null) : PageKeyedDataSource<Int, Movie>() {

    @Inject
    lateinit var apiService: APIService

    init {
        BaseApplication.applicationComponent.inject(this)
    }

    var liveState = MutableLiveData<State>()
    private var genreId = params?.getInt("genreId", 0) ?: 0

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Movie>) {
        val page =  1
        updateState(State.setLoading().setPage(page))

        scope.launch {
            try {
                val apiResponse  = apiService.getMovieByGenre(genreId, page)
                if (apiResponse.success) {
                    if (apiResponse.results.isNullOrEmpty()) {
                        updateState(State.setEmpty().setPage(page))
                    } else {
                        updateState(State.setSuccess().setPage(page))
                        callback.onResult(apiResponse.results!!, null, page + 1)
                    }
                } else {
                    updateState(State.setFailed(apiResponse.status_message).setPage(page))
                }
            } catch (e: Exception) {
                updateState(State.setFailed(e.message).setPage(page))
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        val page = params.key
        updateState(State.setLoading().setPage(page))

        scope.launch {
            try {
                val apiResponse = apiService.getMovieByGenre(genreId, page)
                if (apiResponse.results.isNullOrEmpty()) {
                    updateState(State.setEmpty().setPage(page))
                } else {
                    updateState(State.setSuccess().setPage(page))
                    callback.onResult(apiResponse.results!!,  page + 1)
                }
            } catch (e: Exception) {
                updateState(State.setFailed(e.message).setPage(page))
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) { }

    private fun updateState(state: State) {
        liveState.postValue(state)
    }

    // Factory will interact directly with viewmodel & provides datasource response using LiveData
    class Factory(val scope: CoroutineScope, val args: Bundle? = null) : DataSource.Factory<Int, Movie>() {

        var genreId: Int = 0
        val liveDataSource = MutableLiveData<MoviePagedDataSource>()

        override fun create(): DataSource<Int, Movie> {
            val params = Bundle()
            params.putInt("genreId", args?.getInt("genreId") ?: genreId)
            val pagedDataSource = MoviePagedDataSource(scope, params)
            liveDataSource.postValue(pagedDataSource)
            return pagedDataSource
        }
    }
}