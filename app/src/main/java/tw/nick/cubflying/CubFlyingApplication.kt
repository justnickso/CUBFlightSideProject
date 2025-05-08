package tw.nick.cubflying

import android.app.Application
import tw.nick.cubflying.di.KoinModules

class CubFlyingApplication  : Application() {

    override fun onCreate() {
        super.onCreate()
        KoinModules.initKoin(this)
    }
}