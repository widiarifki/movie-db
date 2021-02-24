package id.widiarifki.movie.presentation.review

import androidx.lifecycle.*
import androidx.paging.PagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import id.widiarifki.movie.data.model.Review
import id.widiarifki.movie.repository.ReviewRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel
@Inject constructor(
    private val repository: ReviewRepository
): ViewModel() {

    fun getReviews(movieId: Int?) : Flow<PagingData<Review>> {
        return repository.getPagingReviews(movieId)
    }

}
