package tw.nick.cubflying.service

import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.view.WindowManager
import org.koin.android.ext.android.inject
import timber.log.Timber
import tw.nick.cubflying.ui.ExchangeRateCalculatorWindow
import tw.nick.cubflying.util.ServiceUtil

class CUBFlyingService : Service() {
    private val windowManager : WindowManager by inject()

    companion object {
        const val NOTIFICATION_ID = 1
    }

    override fun onBind(intent: Intent?): IBinder? {
       return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.d("** onStartCommand - ${intent?.data}")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startForeground(
                NOTIFICATION_ID,
                ServiceUtil.createNotification(this),
                ServiceInfo.FOREGROUND_SERVICE_TYPE_REMOTE_MESSAGING
            )
        } else {
            startForeground(NOTIFICATION_ID, ServiceUtil.createNotification(this))
        }
        return START_NOT_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        ServiceUtil.createNotificationChannel(this)
        ExchangeRateCalculatorWindow().show()
    }


}