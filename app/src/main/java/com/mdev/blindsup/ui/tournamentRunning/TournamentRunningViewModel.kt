package com.mdev.blindsup.ui.tournamentRunning

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import android.os.SystemClock
import androidx.core.app.AlarmManagerCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mdev.blindsup.data.BlindLevels
import com.mdev.blindsup.receiver.AlarmReceiver
import kotlinx.coroutines.launch

class TournamentRunningViewModel(private val app: Application) : AndroidViewModel(app) {

    val alarmManager = app.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    private val minute: Long = 60_000L
    private val second: Long = 1_000L
    private var position = 0
    private var isPaused = false
    private var isNew = true
    private var millisecondLeft = 0L
    private val userSmallBlind = 25
    private val userBigBlind = 50
    private val notificationIntent = Intent(app, AlarmReceiver::class.java)
    private var pendingIntent: PendingIntent = PendingIntent.getBroadcast(
        getApplication(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT
    )
    private lateinit var timer: CountDownTimer

    private val _elapsedTime = MutableLiveData<Long>()
    val elapsedTime: LiveData<Long>
        get() = _elapsedTime

    private val _smallBlind = MutableLiveData<Int>()
    val smallBlind: LiveData<Int>
        get() = _smallBlind

    private val _bigBlind = MutableLiveData<Int>()
    val bigBlind: LiveData<Int>
        get() = _bigBlind

    private val _nextSmallBlind = MutableLiveData<Int>()
    val nextSmallBlind: LiveData<Int>
        get() = _nextSmallBlind

    private val _nextBigBlind = MutableLiveData<Int>()
    val nextBigBlind: LiveData<Int>
        get() = _nextBigBlind

    private val _currentLevel = MutableLiveData<Int>()
    val currentLevel: LiveData<Int>
        get() = _currentLevel

    private val _nextLevel = MutableLiveData<Int>()
    val nextLevel: LiveData<Int>
        get() = _nextLevel


    val blindLevel = BlindLevels()

    init {

        _elapsedTime.value = second * 10
        _smallBlind.value = userSmallBlind
        _bigBlind.value = userBigBlind
        _nextSmallBlind.value = userSmallBlind * 2
        _nextBigBlind.value = userBigBlind * 2
        _currentLevel.value = position + 1
        _nextLevel.value = position + 2

    }


    fun beginTimer() {

        if (isNew) {
            val trigger = SystemClock.elapsedRealtime() + second * 10


//            AlarmManagerCompat.setExactAndAllowWhileIdle(
//                alarmManager, AlarmManager.ELAPSED_REALTIME_WAKEUP, trigger, pendingIntent
//            )

            createTimer()
        } else {
            createTimer()
        }


    }


    fun createTimer() {

//        if (isNew) {
//            isNew = false

        val triggerTime = SystemClock.elapsedRealtime() + second * 10
        // println(time)
        println(_elapsedTime.value)
        timer = object : CountDownTimer(triggerTime, second) {
            override fun onTick(p0: Long) {
                _elapsedTime.value = triggerTime - SystemClock.elapsedRealtime()
                millisecondLeft = _elapsedTime.value!!
                //  println(elapsedTime)
                if (_elapsedTime.value!! <= 0) {
                    resetTimer()
                }
            }

            override fun onFinish() {
                resetTimer()

            }
        }
        timer.start()
//        }

//        else {
//            if (!isPaused) {
//                isPaused = true
//                position++
//                val blindLevel = BlindLevels()
//                _smallBlind.value = blindLevel.blinds[position].smallBlind
//                _bigBlind.value = blindLevel.blinds[position].bigBlind
//                timer = object : CountDownTimer(millisecondLeft, second) {
//                    override fun onTick(p0: Long) {
//                        _elapsedTime.value = millisecondLeft - SystemClock.elapsedRealtime()
//                        if (_elapsedTime.value!! <= 0) {
//                            resetTimer()
//                        }
//                    }
//
//                    override fun onFinish() {
//                        resetTimer()
//                    }
//
//                }
//                timer.start()
//            }
//            else {
//                isPaused = false
//                timer.cancel()
//            }
//        }


    }


    fun resetTimer() {
        position++
        _smallBlind.value = _smallBlind.value!! * 2
        _bigBlind.value = _bigBlind.value!! * 2
        _nextSmallBlind.value = _smallBlind.value!! * 2
        _nextBigBlind.value = _bigBlind.value!! * 2
        _currentLevel.value = position + 1
        _nextLevel.value = position + 2
        timer.cancel()
        _elapsedTime.value = 0

        beginTimer()
    }


}