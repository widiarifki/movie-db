package id.widiarifki.movie.utils.livedata

import androidx.lifecycle.MediatorLiveData

class StatedLiveData<T> : MediatorLiveData<StatedData<T>>() {

    init {
        loading()
    }

    fun loading() {
        postValue(StatedData.loading())
    }

    fun success() {
        postValue(StatedData.success())
    }

    fun load(data: T?) {
        postValue(StatedData.load(data))
    }

    fun error(message: String?) {
        postValue(StatedData.error(message ?: "Global error message"))
    }

    fun error(error: Throwable) {
        postValue(StatedData.error(error))
    }
}