package id.widiarifki.movie.data.model
import com.google.gson.annotations.SerializedName

data class MovieStatus(
    @SerializedName("favorite")
    var favorite: Boolean?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("rated")
    var rated: Boolean?,
    @SerializedName("watchlist")
    var watchlist: Boolean?
)