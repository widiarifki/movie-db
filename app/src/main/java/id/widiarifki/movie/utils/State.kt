package id.widiarifki.movie.utils

class State(val code: Int, var message: String = "") {

    var requestPage: Int = 1
        private set

    companion object {
        private const val LOADING = 1
        private const val SUCCESS = 2
        private const val FAILED = 3
        private const val EMPTY_DATA = 4

        fun setLoading(message: String?= null) : State {
            return State(LOADING,message ?: "Memuat data..")
        }

        fun setSuccess(message: String?= null) : State {
            return State(SUCCESS,message ?: "Sukses")
        }

        fun setFailed(message: String?= null) : State {
            return State(FAILED,message ?: "Gagal memuat data")
        }

        fun setEmpty(message: String?= null) : State {
            return State(EMPTY_DATA,message ?: "Data sudah termuat semua")
        }
    }

    fun setPage(page: Int): State {
        this.requestPage = page
        return this
    }

    fun setMessage(message: String?): State {
        this.message = message ?: "Gagal memuat data"
        return this
    }

    fun isLoading(): Boolean {
        return this.code == LOADING
    }

    fun isSuccess(): Boolean {
        return this.code == SUCCESS
    }

    fun isFail(): Boolean {
        return this.code == FAILED
    }

    fun isLoadEmpty(): Boolean {
        return this.code == EMPTY_DATA
    }
}
