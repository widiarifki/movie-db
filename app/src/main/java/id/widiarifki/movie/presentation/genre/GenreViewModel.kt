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

    val genresLiveData = StatedLiveData<List<Genre>?>()

    init {
        /**
         * Livedata will first get and load genre saved in local/RoomDB,
         * while also making request to retrieve fresh data from network.
         * This way even tho a failure occured while doing network request,
         * the UI will not be empty because genre data had been cached/stored on local
         * as a result from previous success network request
         */
        genresLiveData.addSource(repository.cache) {
            if (it?.isNotEmpty() == true)
                genresLiveData.loaded(it)
        }

        refreshGenre()
    }

    fun refreshGenre() {
        viewModelScope.launch {
            try {
                if (genresLiveData.value?.data.isNullOrEmpty()) {
                    genresLiveData.loading()
                }
                repository.refreshData()
            } catch (e: Exception) {
                genresLiveData.fail(e.message)
            }
        }
    }
}