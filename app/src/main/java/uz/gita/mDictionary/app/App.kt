package uz.gita.mDictionary.app

import android.app.Application
import uz.gita.mDictionary.data.local.database.WordDatabase
import uz.gita.mDictionary.data.local.shared.Shared

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        WordDatabase.init(this)
        Shared.init(this)

    }
}