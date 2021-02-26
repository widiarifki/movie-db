package id.widiarifki.movie.data.model
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.facebook.drawee.view.SimpleDraweeView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

data class Movie(
    @SerializedName("adult")
    var adult: Boolean?,
    @SerializedName("backdrop_path")
    var backdropPath: String?,
    @SerializedName("budget")
    var budget: Int?,
    @SerializedName("genres")
    var genres: List<Genre>?,
    @SerializedName("homepage")
    var homepage: String?,
    @PrimaryKey
    @SerializedName("id")
    var id: Int?,
    @SerializedName("imdb_id")
    var imdbId: String?,
    @SerializedName("original_language")
    var originalLanguage: String?,
    @SerializedName("original_title")
    var originalTitle: String?,
    @SerializedName("overview")
    var overview: String?,
    @SerializedName("popularity")
    var popularity: Double?,
    @SerializedName("poster_path")
    var posterPath: String?,
    @SerializedName("release_date")
    var releaseDate: String?,
    @SerializedName("revenue")
    var revenue: Int?,
    @SerializedName("runtime")
    var runtime: Int?,
    @SerializedName("status")
    var status: String?,
    @SerializedName("tagline")
    var tagline: String?,
    @SerializedName("title")
    var title: String?,
    @SerializedName("video")
    var video: Boolean?,
    @SerializedName("vote_average")
    var voteAverage: Double?,
    @SerializedName("vote_count")
    var voteCount: Int?,

    @Ignore
    var isWatchlist: Boolean?,
    @Ignore
    var ytTrailerKey: String?
) {
    companion object {
        const val POSTER_URL = "https://www.themoviedb.org/t/p/w185"
        const val BACKDROP_URL = "https://www.themoviedb.org/t/p/w500"

        @JvmStatic @BindingAdapter("app:backdropUrl")
        fun setBackdropUrl(view: View, value: String?) {
            when(view) {
                is SimpleDraweeView -> {
                    value?.let {
                        view.setImageURI(String.format("%s%s", BACKDROP_URL, it))
                    }
                }
            }
        }

        @JvmStatic @BindingAdapter("app:posterUrl")
        fun setPosterUrl(view: View, value: String?) {
            when(view) {
                is SimpleDraweeView -> {
                    value?.let {
                        view.setImageURI(String.format("%s%s", POSTER_URL, it))
                    }
                }
            }
        }

        @JvmStatic @BindingAdapter("app:releaseDate")
        fun setReleaseDate(view: View, value: String?) {
            when(view) {
                is TextView -> {
                    value?.let {
                        try {
                            val serverFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                            val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                            serverFormat.parse(it)?.let {
                                view.text = formatter.format(it)
                            }
                        } catch (e: Exception) {
                        }
                    }
                }
            }
        }

        @JvmStatic @BindingAdapter("app:duration")
        fun setDuration(view: View, value: Int?) {
            when(view) {
                is TextView -> {
                    value?.let { serverVal ->
                        var hour = 0
                        var min = 0
                        hour = (serverVal - serverVal.rem(60)) / 60
                        if (serverVal.rem(60) > 0) {
                            min = serverVal.rem(60)
                        }

                        view.text = String.format(
                            "%s%s",
                            if (hour > 0) "$hour hour " else "",
                            if (min > 0) "$min min" else ""
                        )
                    }
                }
            }
        }

        @JvmStatic @BindingAdapter("app:voteScore")
        fun setVoteScore(view: View, value: Double?) {
            when(view) {
                is TextView -> {
                    value?.let {
                        //view.text = String.format("%s / 10", it)
                        view.text = String.format("%s", it)
                    }
                }
                is CircularProgressIndicator -> {
                    value?.let {
                        view.progress = (it * 10).toInt()
                    }
                }
            }
        }

        @JvmStatic @BindingAdapter("app:genres")
        fun setGenres(view: View, value: List<Genre>?) {
            when(view) {
                is TextView -> {
                    value?.let {
                        view.text = it.map { it.name }.joinToString(", ")
                    }
                }
            }
        }
    }
}