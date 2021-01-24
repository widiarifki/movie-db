package id.widiarifki.movie.feature.genre

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import id.widiarifki.movie.data.AppDatabase
import id.widiarifki.movie.data.model.Genre
import id.widiarifki.movie.helper.StatedLiveData
import id.widiarifki.movie.repository.GenreRepository
import kotlinx.coroutines.launch

class GenreViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getInstance(application)
    private val repository = GenreRepository(database.genreDao())

    // livedata variable we want to expose to view
    val liveGenre = StatedLiveData<List<Genre>?>()

    // livedata from Room DB
    private val cacheGenres = repository.cache

    init {
        liveGenre.addSource(cacheGenres) {
            if (it?.isNotEmpty() == true)
                liveGenre.loaded(it)
        }

        refreshGenre()
    }

    fun refreshGenre() {
        viewModelScope.launch {
            try {
                if (liveGenre.value?.data.isNullOrEmpty()) {
                    liveGenre.loading()
                }
                repository.refreshData()
            } catch (e: Exception) {
                liveGenre.fail(e.message)
            }
        }
    }
}