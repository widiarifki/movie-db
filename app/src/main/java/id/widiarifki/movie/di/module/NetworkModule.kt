package id.widiarifki.movie.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import id.widiarifki.movie.data.network.APIService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class  NetworkModule {

    @Provides
    @Singleton
    fun provideApiService(@ApplicationContext context: Context): APIService {
        return APIService.create(context)
    }

}
