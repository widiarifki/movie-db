package id.widiarifki.movie.presentation.movie.list

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import id.widiarifki.movie.data.model.Movie
import id.widiarifki.movie.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel
@Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    fun getMovies(genreId: Int?) : Flow<PagingData<Movie>> {
        return repository.getMoviesByGenre(genreId).cachedIn(viewModelScope)
    }

}
