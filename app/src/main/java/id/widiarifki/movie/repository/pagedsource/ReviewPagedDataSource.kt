package id.widiarifki.movie.repository.pagedsource

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import id.widiarifki.movie.BaseApplication
import id.widiarifki.movie.data.api.APIService
import id.widiarifki.movie.data.model.Review
import id.widiarifki.movie.helper.State
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class ReviewPagedDataSource(val scope: CoroutineScope, val movieId: Int)
    : PageKeyedDataSource<Int, Review>() {

    @Inject
    lateinit var apiService: APIService

    init {
        BaseApplication.applicationComponent.inject(this)
    }

    var liveState = MutableLiveData<State>()

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Review>) {
        val page = 1
        updateState(State.setLoading().setPage(page))

        scope.launch {
            try {
                val apiResponse = apiService.getMovieReviews(movieId, page)
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

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Review>) {
        val page = params.key
        updateState(State.setLoading().setPage(page))

        scope.launch {
            try {
                val apiResponse = apiService.getMovieReviews(movieId, page)
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

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Review>) { }

    private fun updateState(state: State) {
        liveState.postValue(state)
    }

    class Factory(val scope: CoroutineScope) : DataSource.Factory<Int, Review>() {

        var movieId: Int? = null
        val liveDataSource = MutableLiveData<ReviewPagedDataSource>()

        override fun create(): DataSource<Int, Review> {
            val pagedDataSource = ReviewPagedDataSource(scope,movieId ?: 0)
            liveDataSource.postValue(pagedDataSource)
            return pagedDataSource
        }
    }

}