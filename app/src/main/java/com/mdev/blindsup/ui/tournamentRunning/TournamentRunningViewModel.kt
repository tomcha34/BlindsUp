package com.mdev.blindsup.ui.tournamentRunning

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import android.os.SystemClock
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.mdev.blindsup.data.BlindLevels
import com.mdev.blindsup.data.TournamentData
import com.mdev.blindsup.receiver.AlarmReceiver

private const val TAG = "TournamentRunningViewMo"

class TournamentRunningViewModel(private val app: Application) : AndroidViewModel(app) {

    val alarmManager = app.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    private val minute: Long = 60_000L
    private val second: Long = 1_000L
    private var position = 0
    private var isPaused = false
    private var isNew = true
    private var millisecondLeft = 0L
    private var userSmallBlind = 25
    private var userBigBlind = 50
    var userTimeSelection = 0L
    private var blindsList = mutableListOf<TournamentData>()
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

//        _elapsedTime.value = second * 10
//        _smallBlind.value = userSmallBlind
//        _bigBlind.value = userBigBlind
        _nextSmallBlind.value = userSmallBlind * 2
        _nextBigBlind.value = userBigBlind * 2
        _currentLevel.value = position + 1
        _nextLevel.value = position + 2


    }

    fun fetchBlinds(id: String, context: Context) {
        if (id.length > 1) {
            //  println("longer than one")
            val acct = GoogleSignIn.getLastSignedInAccount(context)

            val auth = Firebase.auth
            if (auth.currentUser != null) {
                val databaseBlinds =
                    FirebaseDatabase.getInstance().getReference("Users/${acct.id}/Blinds")

                databaseBlinds.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            for (blindsSnapshot in snapshot.children) {
                                var userBlinds = blindsSnapshot.getValue(TournamentData::class.java)
                                userBlinds?.id = blindsSnapshot.key


                                if (userBlinds != null) {

                                    blindsList.add(userBlinds)
                                    println("added one $userBlinds")
                                }
                            }
                            for (blinds in blindsList) {
                                if (blinds.id == id) {
                                    print("this is the one $blinds")
                                    userTimeSelection = blinds.blindLength * minute
                                    _elapsedTime.value = userTimeSelection
                                    _smallBlind.value = blinds.smallestChip
                                    _bigBlind.value = blinds.smallestChip * 2
                                    _nextSmallBlind.value = blinds.smallestChip * 2
                                    _nextBigBlind.value = blinds.smallestChip * 4
                                }
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e(TAG, "onCancelled: error")
                    }
                })
            }
        } else {
            _elapsedTime.value = second * 10
            _smallBlind.value = userSmallBlind
            _bigBlind.value = userBigBlind
        }
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


        val triggerTime = SystemClock.elapsedRealtime() + userTimeSelection
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