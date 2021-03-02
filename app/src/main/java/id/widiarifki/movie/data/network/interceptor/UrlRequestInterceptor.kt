package id.widiarifki.movie.data.network.interceptor

import id.widiarifki.movie.BuildConfig
import id.widiarifki.movie.data.network.APIConstant
import okhttp3.Interceptor
import okhttp3.Response

class UrlRequestInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val urlBuilder = request.url.newBuilder()
        val modifiedUrl = urlBuilder.addQueryParameter(APIConstant.PARAM_API_KEY, BuildConfig.API_KEY).build()
        request = request.newBuilder().url(modifiedUrl).build()
        return chain.proceed(request)
    }

}
