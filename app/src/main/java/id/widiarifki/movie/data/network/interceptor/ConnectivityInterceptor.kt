package id.widiarifki.movie.data.network.interceptor

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import id.widiarifki.movie.R
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

class ConnectivityInterceptor(val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

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
        return chain.proceed(newRequest)
    }

}