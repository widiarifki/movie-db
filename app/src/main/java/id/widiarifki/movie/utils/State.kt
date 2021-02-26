package id.widiarifki.movie.utils

class State {

    constructor(code: Int) {
        this.code = code
    }
    constructor(code: Int, message: String) {
        this.code = code
    }
    constructor(code: Int, throwable: Throwable) {
        this.code = code
    }

    var code: Int
    var message: String? = null

    companion object {
        const val STATE_LOADING = 1
        const val STATE_SUCCESS = 2
        const val STATE_FAILED = 3

        fun setLoading() : State {
            return State(STATE_LOADING)
        }

        fun setSuccess() : State {
            return State(STATE_SUCCESS)
        }

        fun setFailed(throwable: Throwable? = null) : State {
            // todo: nanti dipertimbangkan error message handler mending dmna?
            return State(STATE_FAILED, throwable?.message ?: "Gagal memuat/memproses data")
            // todo: globalization/localization message
        }

        fun setFailed(message: String?) : State {
            return State(STATE_FAILED, message ?: "Gagal memuat/memproses data")
        }
    }

    fun setMessage(message: String?): State {
        this.message = message ?: "Gagal memuat data"
        return this
    }

    fun isLoading(): Boolean {
        return this.code == STATE_LOADING
    }

    fun isSuccess(): Boolean {
        return this.code == STATE_SUCCESS
    }

    fun isFail(): Boolean {
        return this.code == STATE_FAILED
    }
}
