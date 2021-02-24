package id.widiarifki.movie.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import id.widiarifki.movie.data.local.dao.GenreDao
import id.widiarifki.movie.data.model.Genre

@Database(entities = [Genre::class], version = DatabaseConstant.DB_VERSION)
abstract class AppDatabase : RoomDatabase() {

    abstract fun genreDao() : GenreDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context) : AppDatabase {
            if (instance == null) {
                synchronized(this) {
                    instance = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, DatabaseConstant.DB_NAME)
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }

            return instance!!
        }

    }
}
