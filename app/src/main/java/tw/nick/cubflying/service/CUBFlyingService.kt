package tw.nick.cubflying.service

import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.util.Log
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.inject
import timber.log.Timber
import tw.nick.cubflying.data.RatesInfo
import tw.nick.cubflying.service.floating.FloatingCommand
import tw.nick.cubflying.service.floating.FloatingEventBus
import tw.nick.cubflying.ui.ExchangeRateCalculatorWindow
import tw.nick.cubflying.util.ServiceUtil

class CUBFlyingService : LifecycleService() {
    private var isFloatingWindowShow = false
    private var calculatorWindow: ExchangeRateCalculatorWindow? = null
    private val floatingEventBus: FloatingEventBus by inject(FloatingEventBus::class.java)

    companion object {
        const val NOTIFICATION_ID = 1
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
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
        lifecycleScope.launch {
            floatingEventBus.commands.collect { command ->
                withContext(Dispatchers.Main) {
                    when (command) {
                        is FloatingCommand.ShowFloatingWindow -> showFloatingWindow(command.currency, command.rateInfo)
                        is FloatingCommand.HideFloatingWindow -> hideFloatingWindow()
                    }
                }
            }
        }
    }

    private fun hideFloatingWindow() {
        if (isFloatingWindowShow) {
            calculatorWindow?.removeWindow()
            isFloatingWindowShow = false
        }
    }

    private fun showFloatingWindow(currency: String, ratesInfo: RatesInfo) {
        Log.d("showFloatingWindow", "showFloatingWindow")
        if (!isFloatingWindowShow) {
            calculatorWindow = ExchangeRateCalculatorWindow()
            calculatorWindow?.show()
            calculatorWindow?.setCloseListener {
                calculatorWindow?.removeWindow()
                isFloatingWindowShow = false
            }
            calculatorWindow?.setCurrency(currency, ratesInfo)
            isFloatingWindowShow = true
        }else{
            calculatorWindow?.setCurrency(currency, ratesInfo)
        }

    }


}