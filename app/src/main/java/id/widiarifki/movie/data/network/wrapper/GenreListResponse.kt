package id.widiarifki.movie.data.network.wrapper

import com.google.gson.annotations.SerializedName
import id.widiarifki.movie.data.model.Genre

data class GenreListResponse(
    @SerializedName("genres")
    var results: List<Genre>? = null
)