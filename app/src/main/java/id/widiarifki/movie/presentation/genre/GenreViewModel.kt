package id.widiarifki.movie.presentation.genre

import android.util.Log
import android.view.View
import android.widget.Button
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.widiarifki.movie.data.model.Genre
import id.widiarifki.movie.utils.livedata.StatedLiveData
import id.widiarifki.movie.repository.GenreRepository
import id.widiarifki.movie.utils.livedata.StatedData
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GenreViewModel
@Inject constructor(
    private val repository: GenreRepository
): ViewModel() {

    val genres: StatedLiveData<List<Genre>> = StatedLiveData()
    val enableRetry = true

    private var isRefreshed = false

    init {
        genres.addSource(repository.cache) { cacheData ->
            if (cacheData.isNotEmpty()) genres.load(cacheData)
            refreshData(cacheData)
        }

        /**
         * Livedata will first get and load genre saved in local/RoomDB,
         * while also making request to retrieve fresh data from network.
         * This way even tho a failure occured while doing network request,
         * the UI will not be empty because genre data had been cached/stored on local
         * as a result from previous success network request
         */
    }

    private fun refreshData(cacheData: List<Genre>? = null) {
        while (!isRefreshed) {
            viewModelScope.launch {
                if (cacheData.isNullOrEmpty()) genres.loading()

                genres.addSource(repository.fetchGenreList()) {
                    when {
                        it.isError() -> if (cacheData.isNullOrEmpty()) genres.error(it.message)
                        it.isSuccess() -> if (it.data.isNullOrEmpty()) genres.empty()
                    }
                }
            }
            isRefreshed = true
        }
    }

    fun retry() {
        isRefreshed = false
        refreshData()
    }
}