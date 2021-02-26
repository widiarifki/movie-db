package id.widiarifki.movie.utils

import androidx.lifecycle.MediatorLiveData

class StatedLiveData<T> : MediatorLiveData<StatedData<T>>() {

    var state: StatedData<T> = StatedData()

    init {
        state = StatedData(State.setLoading())
    }

    fun loading() {
        postValue(state.loading())
    }

    fun loaded(data: T? = null) {
        postValue(state.success(data))
    }

    fun success() {
        postValue(state.success())
    }

    fun fail(message: String?) {
        postValue(state.fail(message))
    }

    fun fail(throwable: Throwable?) {
        postValue(state.fail(throwable))
    }

    fun postStateData(data: StatedData<T>) {
        super.postValue(data)
    }
}