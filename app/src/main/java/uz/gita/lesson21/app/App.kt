package uz.gita.lesson21.app

import android.app.Application
import uz.gita.lesson21.data.local.database.WordDatabase
import uz.gita.lesson21.data.local.shared.Shared

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        WordDatabase.init(this)
        Shared.init(this)

    }
}