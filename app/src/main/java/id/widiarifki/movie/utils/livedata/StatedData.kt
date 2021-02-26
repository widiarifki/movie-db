package id.widiarifki.movie.utils.livedata

import android.content.Context
import id.widiarifki.movie.R
import javax.inject.Inject

data class StatedData<T> (
    var status_code: Int? = null,
    private var status_message: String? = null,
    var data: T? = null
) {

    fun isLoading(): Boolean {
        return status_code == STATE_LOADING
    }

    fun isSuccess(): Boolean {
        return status_code == STATE_SUCCESS
    }

    fun isError(): Boolean {
        return status_code == STATE_ERROR
    }

    fun getMessage(): String {
        return status_message.orEmpty()
    }

    fun loading(): StatedData<T> {
        return StatedData.loading()
    }

    fun load(data: T?): StatedData<T> {
        return StatedData.load(data)
    }

    fun success(): StatedData<T> {
        return StatedData.success()
    }

    fun error(message: String?): StatedData<T> {
        return StatedData.error(message)
    }

    companion object {
        const val STATE_LOADING = 1
        const val STATE_SUCCESS = 2
        const val STATE_ERROR = 3

        fun <T> loading(): StatedData<T> {
            return StatedData(STATE_LOADING)
        }

        fun <T> load(data: T?): StatedData<T> {
            return StatedData(STATE_SUCCESS, data = data)
        }

        fun <T> success(): StatedData<T> {
            return StatedData(STATE_SUCCESS)
        }

        fun <T> error(message: String?): StatedData<T> {
            return StatedData(STATE_ERROR, status_message = message)
        }
    }
}