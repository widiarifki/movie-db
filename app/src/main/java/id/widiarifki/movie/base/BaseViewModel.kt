package id.widiarifki.movie.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import id.widiarifki.movie.data.AppDatabase

abstract class BaseViewModel<Repository>(application: Application) : AndroidViewModel(application) {

    protected val database = AppDatabase.getInstance(application)

    abstract val repository: Repository

}