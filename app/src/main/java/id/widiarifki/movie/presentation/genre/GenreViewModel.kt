package id.widiarifki.movie.presentation.genre

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.widiarifki.movie.data.model.Genre
import id.widiarifki.movie.utils.StatedLiveData
import id.widiarifki.movie.repository.GenreRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GenreViewModel
@Inject constructor(
    private val repository: GenreRepository
): ViewModel() {

    private val cacheGenres = repository.cache
    val liveGenres = StatedLiveData<List<Genre>?>()

    init {
        liveGenres.addSource(cacheGenres) {
            if (it?.isNotEmpty() == true)
                liveGenres.loaded(it)
        }

        refreshGenre()
    }

    fun refreshGenre() {
        viewModelScope.launch {
            try {
                if (liveGenres.value?.data.isNullOrEmpty()) {
                    liveGenres.loading()
                }
                repository.refreshData()
            } catch (e: Exception) {
                liveGenres.fail(e.message)
            }
        }
    }
}