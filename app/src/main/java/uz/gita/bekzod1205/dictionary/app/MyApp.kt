package uz.gita.bekzod1205.dictionary.app

import android.app.Application
import uz.gita.bekzod1205.dictionary.db.AppDatabase

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        AppDatabase.init(this)
    }
}