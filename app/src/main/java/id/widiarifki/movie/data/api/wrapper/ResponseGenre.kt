package id.widiarifki.movie.data.api.wrapper

import com.google.gson.annotations.SerializedName
import id.widiarifki.movie.data.model.Genre

data class ResponseGenre(
    @SerializedName("genres")
    var results: List<Genre>? = null
)