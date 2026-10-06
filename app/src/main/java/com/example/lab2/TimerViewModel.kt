package com.example.lab2

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel

class TimerViewModel : ViewModel() {

    companion object {
        private const val DEFAULT_TIME = 30000L
    }

    private var timer: CountDownTimer? = null

    // Час, коли таймер повинен завершитися
    private var endTime: Long = 0L

    var timeLeft: Long = DEFAULT_TIME
        private set

    var isRunning: Boolean = false
        private set

    fun start(
        onTick: (Long) -> Unit,
        onFinish: () -> Unit
    ) {

        if (isRunning || timeLeft <= 0) {
            return
        }

        // Запам'ятовуємо точний момент завершення
        endTime = System.currentTimeMillis() + timeLeft

        startTimer(onTick, onFinish)
    }

    private fun startTimer(
        onTick: (Long) -> Unit,
        onFinish: () -> Unit
    ) {

        timer?.cancel()

        val remainingTime =
            (endTime - System.currentTimeMillis()).coerceAtLeast(0L)

        if (remainingTime <= 0) {

            timeLeft = 0
            isRunning = false
            onFinish()

            return
        }

        isRunning = true

        timer = object : CountDownTimer(
            remainingTime,
            1000
        ) {

            override fun onTick(millisUntilFinished: Long) {

                timeLeft = millisUntilFinished

                onTick(millisUntilFinished)
            }

            override fun onFinish() {

                timeLeft = 0
                isRunning = false
                timer = null

                onFinish()
            }

        }.start()
    }

    fun stop() {

        timer?.cancel()
        timer = null

        // Зберігаємо фактичний залишок часу
        if (isRunning) {

            timeLeft =
                (endTime - System.currentTimeMillis())
                    .coerceAtLeast(0L)
        }

        isRunning = false
    }

    fun reset() {

        timer?.cancel()
        timer = null

        timeLeft = DEFAULT_TIME
        endTime = 0L
        isRunning = false
    }

    fun restore(
        onTick: (Long) -> Unit,
        onFinish: () -> Unit
    ) {

        if (!isRunning) {
            return
        }

        startTimer(onTick, onFinish)
    }

    override fun onCleared() {

        timer?.cancel()

        super.onCleared()
    }
}