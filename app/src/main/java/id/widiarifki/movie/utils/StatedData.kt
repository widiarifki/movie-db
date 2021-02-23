package id.widiarifki.movie.utils

class StatedData<T> {

    lateinit var state: State
    var data: T? = null
    var message: String = ""
        get() {
            return if(state.message.isEmpty()) "Tampaknya terjadi kesalahan" else state.message
        }
        set(value) {
            field = value
            state.setMessage(value)
        }

    constructor()
    constructor(state: State, data: T? = null) {
        this.state = state
        this.data = data
    }

    fun isLoading() : Boolean {
        return state.isLoading()
    }

    fun isSuccess() : Boolean {
        return state.isSuccess()
    }

    fun isFail() : Boolean {
        return state.isFail()
    }
}