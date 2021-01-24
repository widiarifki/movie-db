package id.widiarifki.movie.data.model

import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class Author(
    @SerializedName("avatar_path")
    var avatarPath: String?,
    @SerializedName("name")
    var name: String?,
    @SerializedName("rating")
    var rating: Double?,
    @PrimaryKey
    @SerializedName("username")
    var username: String?
)