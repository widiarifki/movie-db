package id.widiarifki.movie.data.model
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import id.widiarifki.movie.data.DatabaseConstant

@Entity(tableName = DatabaseConstant.TBL_GENRE)
data class Genre(
    @PrimaryKey
    @SerializedName("id")
    var id: Int?,
    @SerializedName("name")
    var name: String?
)