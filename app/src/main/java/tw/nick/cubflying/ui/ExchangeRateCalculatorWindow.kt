package tw.nick.cubflying.ui

import android.graphics.PixelFormat
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import org.koin.java.KoinJavaComponent.inject
import tw.nick.cubflying.databinding.WindowExchangeRateCalculatorBinding
import tw.nick.cubflying.extension.toDp

class ExchangeRateCalculatorWindow {
    private val windowManager: WindowManager by inject(WindowManager::class.java)
    private val layoutInflater: LayoutInflater by inject(LayoutInflater::class.java)
    private lateinit var binding: WindowExchangeRateCalculatorBinding

    private lateinit var params: WindowManager.LayoutParams

    fun show(): WindowExchangeRateCalculatorBinding {
        binding = WindowExchangeRateCalculatorBinding.inflate(layoutInflater)
        setButton()
        binding.layoutWindow.setOnTouchListener(windowTouchMoveListener)
        params = WindowManager.LayoutParams(
            400.toDp,
            400.toDp,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else
                WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = 50
            y = 100
        }
        windowManager.addView(binding.root, params)
        return binding
    }

    private fun setButton() {
       with(binding){
           btnClose.setOnClickListener { windowManager.removeView(binding.root) }
           btn1.setOnClickListener { windowManager.removeView(binding.root) }
       }
    }


    private val windowTouchMoveListener = object : View.OnTouchListener {
        private var initialX = 0
        private var initialY = 0
        private var initialTouchX = 0f
        private var initialTouchY = 0f

        override fun onTouch(v: View, event: MotionEvent): Boolean {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    v.performClick()
                    initialX = params.x
                    initialY = params.y
                    initialTouchX = event.rawX
                    initialTouchY = event.rawY
                    return true
                }

                MotionEvent.ACTION_MOVE -> {
                    params.x = initialX + (event.rawX - initialTouchX).toInt()
                    params.y = initialY + (event.rawY - initialTouchY).toInt()
                    windowManager.updateViewLayout(binding.root, params)
                    return true
                }
            }
            return false
        }

    }


}