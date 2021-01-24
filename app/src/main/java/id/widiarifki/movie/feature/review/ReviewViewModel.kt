package id.widiarifki.movie.feature.review

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import id.widiarifki.movie.data.model.Review
import id.widiarifki.movie.repository.pagedsource.ReviewPagedDataSource
import id.widiarifki.movie.helper.State

class ReviewViewModel(application: Application): AndroidViewModel(application) {

    private var dataSourceFactory = ReviewPagedDataSource.Factory(viewModelScope)
    var livePagedList: LiveData<PagedList<Review>>
    var liveRequestState: LiveData<State>

    init {
        livePagedList = LivePagedListBuilder(dataSourceFactory, Review.DEFAULT_PAGE_SIZE).build()
        liveRequestState = Transformations.switchMap(dataSourceFactory.liveDataSource) {
            it.liveState
        }
    }

    fun setMovieId(movieId: Int) {
        dataSourceFactory.movieId = movieId
        dataSourceFactory.liveDataSource.value?.invalidate()
    }

    fun retry() {
        dataSourceFactory.liveDataSource.value?.invalidate()
    }

}
