package id.widiarifki.movie.di.module

import androidx.viewbinding.BuildConfig
import dagger.Module
import dagger.Provides
import id.widiarifki.movie.BuildConfig.BASE_URL
import id.widiarifki.movie.data.api.APIService
import id.widiarifki.movie.di.scope.ApplicationScope
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module(includes = [(NetworkModule::class)])
class APIServiceModule {

    @Provides
    @ApplicationScope
    fun apiService(retrofit: Retrofit): APIService = retrofit.create(APIService::class.java)

    @Provides
    @ApplicationScope
    fun retrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .baseUrl(BASE_URL)
        .build()

}
