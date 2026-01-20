package uz.gita.bekzod1205.dictionary.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import uz.gita.bekzod1205.dictionary.db.dao.DictionaryDao
import uz.gita.bekzod1205.dictionary.db.entity.WordEntity

@Database(entities = [WordEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getDictionaryDao(): DictionaryDao

    companion object {
        private lateinit var instance: AppDatabase

        fun init(context: Context) {
            if (!::instance.isInitialized) {
                instance = Room.databaseBuilder(context, AppDatabase::class.java, "dictionary.db")
                    .createFromAsset("Dictionary.db").allowMainThreadQueries().build()
            }
        }

        fun getInstance(): AppDatabase = instance
    }
}