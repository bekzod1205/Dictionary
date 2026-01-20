package uz.gita.bekzod1205.dictionary.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "dictionary")
data class WordEntity(
    @PrimaryKey
    val id: Int,
    val english: String?,
    val type: String?,
    val transcript: String?,
    val uzbek: String?,
    val countable: String?,
    @ColumnInfo(name = "is_favourite")
    val isFavourite: Int = 0
) : Serializable