package tw.nick.cubflying.ui

import android.app.Service.LAYOUT_INFLATER_SERVICE
import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat.getSystemService
import org.koin.java.KoinJavaComponent.inject
import tw.nick.cubflying.data.RatesInfo
import tw.nick.cubflying.databinding.WindowExchangeRateCalculatorBinding
import tw.nick.cubflying.extension.toBigDecimal
import tw.nick.cubflying.extension.toDp
import tw.nick.cubflying.extension.toExchangeBigDecimal
import tw.nick.cubflying.service.floating.FloatingCommand
import tw.nick.cubflying.service.floating.FloatingEventBus
import java.math.BigDecimal
import java.math.RoundingMode

class ExchangeRateCalculatorWindow {
    private val windowManager: WindowManager by inject(WindowManager::class.java)
    private val layoutInflater: LayoutInflater by inject(LayoutInflater::class.java)
    private lateinit var binding: WindowExchangeRateCalculatorBinding
    private lateinit var params: WindowManager.LayoutParams
    private var closeListener: (() -> Unit)? = null

    private val maxLength = 15
    private var currentText: String = ""
    private var rate = 1.0

    fun show() {
        val newBinding = WindowExchangeRateCalculatorBinding.inflate(layoutInflater)
        binding = newBinding
        setButton()
        params = WindowManager.LayoutParams(
            400.toDp,
            430.toDp,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else
                WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = 50
            y = 400.toDp
        }

        windowManager.addView(binding.root, params)
    }

    private fun setButton() {
        with(binding) {
            setupNumberPad()
            layoutWindow.setOnTouchListener(windowTouchMoveListener)
            btnClose.setOnClickListener {
                closeListener?.invoke()
            }
        }
    }

    fun removeWindow() {
        windowManager.removeView(binding.root)
    }

    fun setCloseListener(closeListener: () -> Unit) {
        this.closeListener = closeListener
    }

    fun setCurrency(currency: String, exchangeRatesInfo: RatesInfo) {
        binding.tvCurrencyName.text = currency
        binding.tvExchangeCurrencyName.text = exchangeRatesInfo.currency
        rate = exchangeRatesInfo.rate
        if (currentText.isEmpty()) {
            currentText = "1"
            updateDisplay()
        }else{
            updateDisplay()
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

    private fun setupNumberPad() {
        val numberButtons = mapOf(
            binding.btn0 to "0",
            binding.btn1 to "1",
            binding.btn2 to "2",
            binding.btn3 to "3",
            binding.btn4 to "4",
            binding.btn5 to "5",
            binding.btn6 to "6",
            binding.btn7 to "7",
            binding.btn8 to "8",
            binding.btn9 to "9",
            binding.btnDot to "."
        )

        numberButtons.forEach { (button, value) ->
            button.setOnClickListener {
                onKeyPress(value)
            }
        }

        binding.btnC.setOnClickListener {
            onClear()
        }

        // 初始更新畫面
        updateDisplay()
    }

    private fun onKeyPress(input: String) {
        if (input == "." && currentText.contains(".")) return
        if (currentText.length >= maxLength) return

        // 避免開頭就是小數點
        if (input == "." && currentText.isEmpty()) {
            currentText = "0."
        } else {
            currentText += input
        }

        updateDisplay()
    }

    private fun onClear() {
        currentText = ""
        updateDisplay()
    }

    private fun updateDisplay() {
        binding.tvAmount.text = currentText.ifEmpty { "0" }
        if (currentText.isNotEmpty()) {
            binding.tvExchangeAmount.text =
                currentText.toDouble().toExchangeBigDecimal(rate).toPlainString()
        }
    }


}