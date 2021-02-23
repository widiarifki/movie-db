package id.widiarifki.movie.data.network.wrapper

import com.google.gson.annotations.SerializedName

data class ResponseList<T>(
    var id: Int? = null,
    var page: Int? = null,
    @SerializedName("results")
    var results: List<T>? = null,
    var total_pages: Int? = null,
    var total_results: Int? = null,
    var success: Boolean = true, // by default
    var status_message: String? = null
)