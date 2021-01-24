package id.widiarifki.movie.feature.movie.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import id.widiarifki.movie.data.model.Movie
import id.widiarifki.movie.repository.pagedsource.MoviePagedDataSource
import id.widiarifki.movie.helper.State
import id.widiarifki.movie.repository.MovieRepository

class MovieListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = MovieRepository()

    private var dataSourceFactory = MoviePagedDataSource.Factory(viewModelScope)
    var livePagedList: LiveData<PagedList<Movie>>
    var liveRequestState: LiveData<State>

    init {
        livePagedList = LivePagedListBuilder(dataSourceFactory, Movie.DEFAULT_PAGE_SIZE).build()
        liveRequestState = Transformations.switchMap(dataSourceFactory.liveDataSource) {
            it.liveState
        }
    }

    fun setGenreId(genreId: Int) {
        dataSourceFactory.genreId = genreId
        dataSourceFactory.liveDataSource.value?.invalidate()
    }

    fun retry() {
        dataSourceFactory.liveDataSource.value?.invalidate()
    }


}
