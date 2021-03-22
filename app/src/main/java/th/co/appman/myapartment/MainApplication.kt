package th.co.appman.myapartment

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import th.co.appman.myapartment.di.databaseModule
import th.co.appman.myapartment.di.viewModelModule

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@MainApplication)
            modules(listOf(databaseModule, viewModelModule))
        }
    }
}