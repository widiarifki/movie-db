package id.widiarifki.movie.helper

import androidx.lifecycle.MediatorLiveData

class StatedLiveData<T> : MediatorLiveData<StatedData<T>>() {

    fun loading(message: String? = null) {
        val state = State.setLoading(message)
        postValue(StatedData(state))
    }

    fun loaded(data: T? = null, message: String? = null) {
        val state = State.setSuccess(message)
        postValue(StatedData(state, data))
    }

    fun fail(message: String?) {
        val state = State.setFailed(message)
        postValue(StatedData(state))
    }

}