package id.widiarifki.movie.di.component

import dagger.Component
import id.widiarifki.movie.repository.pagedsource.MoviePagedDataSource
import id.widiarifki.movie.di.module.APIServiceModule
import id.widiarifki.movie.di.scope.ApplicationScope
import id.widiarifki.movie.repository.GenreRepository
import id.widiarifki.movie.repository.MovieRepository
import id.widiarifki.movie.repository.pagedsource.ReviewPagedDataSource

@ApplicationScope
@Component(modules = [(APIServiceModule::class)])
interface ApplicationComponent {
    fun inject(genreRepository: GenreRepository)
    fun inject(movieRepository: MovieRepository)
    fun inject(moviePagedDataSource: MoviePagedDataSource)
    fun inject(reviewPagedDataSource: ReviewPagedDataSource)
}