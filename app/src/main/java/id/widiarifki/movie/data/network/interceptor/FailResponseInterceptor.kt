package id.widiarifki.movie.data.network.interceptor

import android.content.Context
import com.google.gson.JsonParser
import id.widiarifki.movie.R
import id.widiarifki.movie.data.network.APIConstant
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class FailResponseInterceptor(val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val responseBody = chain.proceed(request).body
        val responseAsObj = JsonParser.parseString(responseBody?.string()).asJsonObject
        val apiSuccess = responseAsObj?.get(APIConstant.FIELD_SUCCESS)?.asBoolean ?: true
        if (!apiSuccess) {
            throw IOException(
                    responseAsObj?.get(APIConstant.FIELD_STATUS_MESSAGE)?.asString ?: context.getString(R.string.msg_error_global)
            )
        }

        val newRequest = request.newBuilder().build()
        return chain.proceed(newRequest)
    }

}
