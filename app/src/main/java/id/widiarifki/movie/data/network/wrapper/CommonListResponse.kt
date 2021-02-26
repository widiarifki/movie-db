package id.widiarifki.movie.data.network.wrapper

data class CommonListResponse<T>(
    var page: Int? = null,
    var results: List<T>? = null,
    var total_pages: Int? = null,
    var total_results: Int? = null
)