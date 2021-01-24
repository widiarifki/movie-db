package id.widiarifki.movie.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import id.widiarifki.movie.data.DatabaseConstant
import id.widiarifki.movie.data.model.Genre

@Dao
interface GenreDao {

    @Query("SELECT * from ${DatabaseConstant.TBL_GENRE}")
    fun getAllGenre() : LiveData<List<Genre>?>

    @Query("DELETE from ${DatabaseConstant.TBL_GENRE}")
    suspend fun deleteAll()

    @Insert
    suspend fun insert(genres: List<Genre>)

}
