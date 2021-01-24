package id.widiarifki.movie.di.module

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import dagger.Module
import dagger.Provides
import id.widiarifki.movie.BuildConfig
import id.widiarifki.movie.R
import id.widiarifki.movie.di.scope.ApplicationScope
import okhttp3.Cache
import okhttp3.ConnectionPool
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

@Module(includes = [(ContextModule::class)])
class NetworkModule {

    @Provides
    @ApplicationScope
    fun file(context: Context): File = File(context.cacheDir, "okhttp_cache")

    @Provides
    @ApplicationScope
    fun cache(file: File): Cache = Cache(file, 10*1000*1000)

    @Provides
    @ApplicationScope
    fun okHttpClient(cache: Cache, context: Context): OkHttpClient = getOkHttpClient(cache, context)

    @Provides
    @ApplicationScope
    fun loggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    private fun getOkHttpClient(cache: Cache, context: Context): OkHttpClient {
        val requestInterceptor = Interceptor { chain ->
            var request = chain.request()
            val url = request.url().newBuilder().addQueryParameter("api_key", BuildConfig.API_KEY).build()
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
                            else -> throw IOException(context.getString(R.string.error_no_network_connection))
                        }
                    } ?: run {
                        throw IOException(context.getString(R.string.error_no_network_connection))
                    }
                } ?: run {
                    throw IOException(context.getString(R.string.error_no_network_connection))
                }
            } else {
                connectivityManager.activeNetworkInfo?.run {
                    when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> throw IOException(context.getString(R.string.error_no_network_connection))
                    }
                }
            }

            val newRequest = chain.request().newBuilder().build()
            chain.proceed(newRequest)
        }

        return OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .connectionPool(ConnectionPool(0,1,TimeUnit.NANOSECONDS))
            .addInterceptor(loggingInterceptor())
            .addInterceptor(connectionInterceptor)
            .addNetworkInterceptor(requestInterceptor)
            .cache(cache).build()
    }
}
