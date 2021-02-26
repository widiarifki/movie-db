package id.widiarifki.movie.data.network

import android.content.Context
import id.widiarifki.movie.BuildConfig
import id.widiarifki.movie.data.model.MovieStatus
import id.widiarifki.movie.data.network.wrapper.GenreListResponse
import id.widiarifki.movie.data.network.wrapper.CommonListResponse
import id.widiarifki.movie.data.model.Movie
import id.widiarifki.movie.data.model.Review
import id.widiarifki.movie.data.model.Video
import id.widiarifki.movie.data.network.wrapper.PostResponse
import okhttp3.ConnectionPool
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
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
    ): Response<Movie>

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

    @FormUrlEncoded
    @POST("account/{account_id}/watchlist")
    suspend fun addToWatchlist(
            @Path("account_id") accountId: String = BuildConfig.ACCOUNT_ID,
            @Query("session_id") sessionId: String = BuildConfig.SESSION_ID,
            @Field("media_type") mediaType: String = "movie",
            @Field("watchlist") watchList: Boolean = true,
            @Field("media_id") mediaId: Int?
    ): PostResponse

    @FormUrlEncoded
    @POST("account/{account_id}/watchlist")
    suspend fun removeFromWatchlist(
            @Path("account_id") accountId: String = BuildConfig.ACCOUNT_ID,
            @Query("session_id") sessionId: String = BuildConfig.SESSION_ID,
            @Field("media_type") mediaType: String = "movie",
            @Field("watchlist") watchList: Boolean = false,
            @Field("media_id") mediaId: Int?
    ): PostResponse

    @GET("movie/{movie_id}/account_states")
    suspend fun getMovieStatus(
            @Path("movie_id") movieId: Int?,
            @Query("session_id") sessionId: String = BuildConfig.SESSION_ID
    ): MovieStatus


    companion object {

        fun create(context: Context) : APIService {

            val logger = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val requestInterceptor = Interceptor { chain ->
                var request = chain.request()
                val url = request.url.newBuilder().addQueryParameter("api_key", BuildConfig.API_KEY).build()
                request = request.newBuilder().url(url).build()
                chain.proceed(request)
            }


            val httpClient = OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .connectionPool(ConnectionPool(0,1, TimeUnit.NANOSECONDS))
                .addInterceptor(logger)
                //.addInterceptor(connectionInterceptor)
                .addNetworkInterceptor(requestInterceptor)
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