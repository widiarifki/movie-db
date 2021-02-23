package id.widiarifki.movie.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import id.widiarifki.movie.BuildConfig
import id.widiarifki.movie.R
import id.widiarifki.movie.data.network.wrapper.ResponseGenre
import id.widiarifki.movie.data.network.wrapper.ResponseList
import id.widiarifki.movie.data.model.Movie
import id.widiarifki.movie.data.model.Review
import id.widiarifki.movie.data.model.Video
import okhttp3.ConnectionPool
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.io.IOException
import java.util.concurrent.TimeUnit

interface APIService {

    @GET("genre/movie/list")
    suspend fun getGenre() : ResponseGenre

    @GET("discover/movie")
    suspend fun getMovieByGenre(
        @Query("with_genres") genreId: Int?,
        @Query("page") page: Int
    ): ResponseList<Movie>

    @GET("movie/{id}")
    suspend fun getMovieDetail(
        @Path("id") id: Int
    ): Response<Movie>

    @GET("movie/{id}/videos")
    suspend fun getMovieVideos(
        @Path("id") id: Int
    ): ResponseList<Video>

    @GET("movie/{id}/reviews")
    suspend fun getMovieReviews(
        @Path("id") id: Int?,
        @Query("page") page: Int
    ): ResponseList<Review>


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

            val connectionInterceptor = Interceptor { chain ->
                val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    connectivityManager.activeNetwork?.let {
                        val actNw = connectivityManager.getNetworkCapabilities(it)
                        actNw?.let {
                            when {
                                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                                else -> throw IOException(context.getString(R.string.msg_error_no_connection))
                            }
                        } ?: run {
                            throw IOException(context.getString(R.string.msg_error_no_connection))
                        }
                    } ?: run {
                        throw IOException(context.getString(R.string.msg_error_no_connection))
                    }
                } else {
                    connectivityManager.activeNetworkInfo?.run {
                        when (type) {
                            ConnectivityManager.TYPE_WIFI -> true
                            ConnectivityManager.TYPE_MOBILE -> true
                            ConnectivityManager.TYPE_ETHERNET -> true
                            else -> throw IOException(context.getString(R.string.msg_error_no_connection))
                        }
                    }
                }

                val newRequest = chain.request().newBuilder().build()
                chain.proceed(newRequest)
            }

            val httpClient = OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .connectionPool(ConnectionPool(0,1, TimeUnit.NANOSECONDS))
                .addInterceptor(logger)
                .addInterceptor(connectionInterceptor)
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