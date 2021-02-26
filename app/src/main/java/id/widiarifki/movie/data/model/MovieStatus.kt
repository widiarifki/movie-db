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
    var watchlist: Boolean?,

    // In case HTTP result is error
    var success: Boolean = true,
    var status_code: Int?,
    var status_message: String?
) {

    var data = this

}