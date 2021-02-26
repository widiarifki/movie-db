package id.widiarifki.movie.presentation.watchlist

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import id.widiarifki.movie.data.model.Movie
import id.widiarifki.movie.repository.MovieRepository
import id.widiarifki.movie.repository.ReviewRepository
import id.widiarifki.movie.repository.WatchlistRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class WatchlistViewModel
@Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    fun getPagingWatchlist() : Flow<PagingData<Movie>> {
        return repository.getPagingWatchlist()
    }
}