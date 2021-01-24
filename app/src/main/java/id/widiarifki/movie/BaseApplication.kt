package id.widiarifki.movie

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco
import id.widiarifki.movie.di.component.ApplicationComponent
import id.widiarifki.movie.di.component.DaggerApplicationComponent
import id.widiarifki.movie.di.module.ContextModule

class BaseApplication : Application() {

    companion object {
        lateinit var applicationComponent: ApplicationComponent
    }

    override fun onCreate() {
        super.onCreate()

        applicationComponent = DaggerApplicationComponent.builder().contextModule(
            ContextModule(this)
        ).build()

        Fresco.initialize(this)
    }
}