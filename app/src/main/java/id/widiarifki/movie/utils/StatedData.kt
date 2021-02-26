package id.widiarifki.movie.utils

class StatedData<T> {

    var state: State = State.setLoading()
    var data: T? = null
    var message: String = ""
        get() {
            return if(state.message.isNullOrEmpty()) {
                when (state.code) {
                    State.STATE_LOADING -> "Loading"
                    State.STATE_FAILED -> "Failed"
                    State.STATE_SUCCESS -> "Success"
                    else -> "State & message unknown"
                }
            } else state.message.orEmpty()
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

    /**
     * new
     */
    fun loading(): StatedData<T> {
        return StatedData(State.setLoading())
    }
    fun success(data: T? = null): StatedData<T> {
        return StatedData(State.setSuccess(), data)
    }
    fun fail(throwable: Throwable?): StatedData<T> {
        return StatedData(State.setFailed(throwable))
    }
    fun fail(message: String?): StatedData<T> {
        return StatedData(State.setFailed(message))
    }
}