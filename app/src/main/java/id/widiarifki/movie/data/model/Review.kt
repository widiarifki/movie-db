package id.widiarifki.movie.data.model
import android.text.format.DateUtils
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.room.PrimaryKey
import com.facebook.drawee.view.SimpleDraweeView
import com.google.gson.annotations.SerializedName
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalField


data class Review(
    @SerializedName("author")
    var author: String?,
    @SerializedName("author_details")
    var authorDetails: Author?,
    @SerializedName("content")
    var content: String?,
    @SerializedName("created_at")
    var createdAt: String?,
    @PrimaryKey
    @SerializedName("id")
    var id: String?,
    @SerializedName("updated_at")
    var updatedAt: String?,
    @SerializedName("url")
    var url: String?
) {
    companion object {
        const val PROFILE_URL = "http://image.tmdb.org/t/p/w45"
        const val DEFAULT_PAGE_SIZE = 20

        @JvmStatic @BindingAdapter("app:avatarPath")
        fun setAvatarPath(view: View, value: String?) {
            when(view) {
                is SimpleDraweeView -> {
                    value?.let {
                        if (it.contains("https://")) {
                            view.setImageURI(it.replaceFirst("/", ""))
                        } else {
                            view.setImageURI(String.format("%s%s", PROFILE_URL, it))
                        }
                    }
                }
            }
        }

        @JvmStatic @BindingAdapter("app:displayDate")
        fun setDisplayDate(view: View, value: String?) {
            when(view) {
                is TextView -> {
                    value?.let {
                        try {
                            val serverFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                            val dateTime = LocalDateTime.parse(it, serverFormat)
                            dateTime?.let {
                                val time = it.toInstant(ZoneOffset.UTC).toEpochMilli()
                                view.text = DateUtils.getRelativeTimeSpanString(time, System.currentTimeMillis(), 0L, DateUtils.FORMAT_ABBREV_ALL)
                            }
                        } catch (e: Exception) {
                        }
                    }
                }
            }
        }
    }
}