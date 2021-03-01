package id.widiarifki.movie.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import id.widiarifki.movie.BuildConfig
import id.widiarifki.movie.R
import id.widiarifki.movie.data.model.MovieStatus
import id.widiarifki.movie.data.network.wrapper.GenreListResponse
import id.widiarifki.movie.data.network.wrapper.CommonListResponse
import id.widiarifki.movie.data.model.Movie
import id.widiarifki.movie.data.model.Review
import id.widiarifki.movie.data.model.Video
import id.widiarifki.movie.data.network.interceptor.ConnectivityInterceptor
import id.widiarifki.movie.data.network.interceptor.FailResponseInterceptor
import id.widiarifki.movie.data.network.interceptor.UrlRequestInterceptor
import id.widiarifki.movie.data.network.wrapper.PostResponse
import okhttp3.ConnectionPool
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.internal.connection.ConnectInterceptor
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.io.IOException
import java.util.concurrent.TimeUnit

interface APIService {

    @GET("genre/movie/list")
    suspend fun getGenre() : GenreListResponse

    @GET("discover/movie")
    suspend fun getMovieByGenre(
        @Query("with_genres") genreId: Int?,
        @Query("page") page: Int
    ): CommonListResponse<Movie>

    @GET("movie/{id}")
    suspend fun getMovieDetail(
        @Path("id") id: Int
    ): Movie

    @GET("movie/{id}/videos")
    suspend fun getMovieVideos(
        @Path("id") id: Int
    ): CommonListResponse<Video>

    @GET("movie/{id}/reviews")
    suspend fun getMovieReviews(
        @Path("id") id: Int?,
        @Query("page") page: Int
    ): CommonListResponse<Review>

    @GET("account/{account_id}/watchlist/movies")
    suspend fun getWatchlist(
            @Path("account_id") accountId: String = BuildConfig.ACCOUNT_ID,
            @Query("session_id") sessionId: String = BuildConfig.SESSION_ID,
            @Query("page") page: Int
    ): CommonListResponse<Movie>

    @GET("movie/{movie_id}/account_states")
    suspend fun getMovieStatus(
            @Path("movie_id") movieId: Int?,
            @Query("session_id") sessionId: String = BuildConfig.SESSION_ID
    ): MovieStatus

    @FormUrlEncoded
    @POST("account/{account_id}/watchlist")
    suspend fun updateWatchlist(
            @Path("account_id") accountId: String = BuildConfig.ACCOUNT_ID,
            @Query("session_id") sessionId: String = BuildConfig.SESSION_ID,
            @Field("media_type") mediaType: String = "movie",
            @Field("watchlist") watchList: Boolean,
            @Field("media_id") mediaId: Int?
    ): PostResponse


    companion object {

        fun create(context: Context) : APIService {
            val httpClient = OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .connectionPool(ConnectionPool(0, 1, TimeUnit.NANOSECONDS))
                    .addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
                    .addInterceptor(FailResponseInterceptor(context))
                    .addInterceptor(ConnectivityInterceptor(context))
                    .addNetworkInterceptor(UrlRequestInterceptor())
                    .build()

            return Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient)
                    .build()
                    .create(APIService::class.java)
        }
    }
}