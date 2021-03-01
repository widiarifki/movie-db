package id.widiarifki.movie.data.network.wrapper

data class PostResponse(
    var success: Boolean? = true,
    var status_code: Int?,
    var status_message: String?
)