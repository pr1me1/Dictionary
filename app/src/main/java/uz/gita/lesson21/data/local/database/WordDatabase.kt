package uz.gita.lesson21.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import uz.gita.lesson21.data.local.daos.WordDao
import uz.gita.lesson21.data.local.model.WordEntity


@Database(entities = [WordEntity::class], version = 1, exportSchema = false)
abstract class WordDatabase: RoomDatabase() {
    abstract fun getWordDao(): WordDao


    companion object{
        private lateinit var instance: WordDatabase

        fun init(context: Context) {
            if (!Companion::instance.isInitialized) {
                instance = Room.databaseBuilder(context, WordDatabase::class.java, "dictionary.db")
                    .createFromAsset("dictionary.db")
                    .allowMainThreadQueries()
                    .build()
            }
        }

        fun getInstance() = instance!!

    }



}