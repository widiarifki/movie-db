package id.widiarifki.movie.data.network.wrapper

data class CommonListResponse<T>(
    var success: Boolean = true,
    var status_message: String? = null,
    var page: Int? = null,
    var results: List<T>? = null,
    var total_pages: Int? = null,
    var total_results: Int? = null
)