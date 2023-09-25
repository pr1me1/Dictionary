package uz.gita.lesson21.data.local.daos

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update


@Dao
interface WordDao {

    @Query("Select * from dictionary")
    fun getAllWords(): Cursor

    @Query("Select * from dictionary where english like :searched || '%'")
    fun searchViaEnglish(searched: String): Cursor

    @Query("Select * from dictionary where english like :searched || '%' and is_favourite==1")
    fun searchViaEnglishSaved(searched: String): Cursor


    @Query("Select * from dictionary where uzbek like :searched ||'%'")
    fun searchViaUzbek(searched: String): Cursor

    @Query("Update dictionary set is_favourite = :newFav where id= :pos")
    fun updateFav(pos: Int, newFav: Int)

    @Query("Select * from dictionary where is_favourite ==1")
    fun getSavedWords(): Cursor

}