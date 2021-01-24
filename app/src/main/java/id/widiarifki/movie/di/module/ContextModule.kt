package id.widiarifki.movie.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import id.widiarifki.movie.di.scope.ApplicationScope

@Module
class ContextModule(context: Context) {

    private val appContext = context.applicationContext

    @Provides
    @ApplicationScope
    fun context(): Context {
        return appContext
    }
}
