package uz.gita.bekzod1205.dictionary.db.dao

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Query

@Dao
interface DictionaryDao {
    @Query("SELECT * FROM dictionary")
    fun getAllWords(): Cursor

    @Query("SELECT * FROM dictionary WHERE english LIKE '%' || :query || '%'")
    fun getWordsByQuery(query: String): Cursor

    @Query("UPDATE dictionary SET is_favourite = :isFavourite WHERE id = :id")
    fun update(id: Int, isFavourite: Int)

    @Query("SELECT * FROM dictionary WHERE is_favourite = 1")
    fun getFavourites(): Cursor

    @Query("SELECT * FROM dictionary WHERE english LIKE '%' || :query || '%' AND is_favourite = 1")
    fun getFavouriteWordsByQuery(query: String): Cursor

}