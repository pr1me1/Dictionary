package uz.gita.lesson21.data.local.shared

import android.content.Context
import android.content.SharedPreferences

class Shared private constructor(context: Context){
    private val preferences: SharedPreferences
    private val editor: SharedPreferences.Editor

    init {
        preferences = context.getSharedPreferences("languageChanger", Context.MODE_PRIVATE)
        editor = preferences.edit()
    }

    fun setLanguage(int: Int) {
        editor.remove("lang").apply()
        editor.putInt("lang", int).apply()
    }

    fun getLanguage(): Int {
        return preferences.getInt("lang", 0)
    }

    companion object{
        lateinit var mypref: Shared


        fun init(context: Context) {
            if (!Companion::mypref.isInitialized) {
                mypref = Shared(context)
            }
        }

        fun getShared(): Shared = mypref

    }
}