package id.widiarifki.movie.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import id.widiarifki.movie.data.AppDatabase
import id.widiarifki.movie.data.dao.GenreDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule() {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    fun provideGenreDao(appDatabase: AppDatabase): GenreDao {
        return appDatabase.genreDao()
    }

}
