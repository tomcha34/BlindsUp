package com.mdev.blindsup.ui.tournamentRunning

import android.app.Application
import android.content.Context
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
import com.mdev.blindsup.data.TournamentData
import com.mdev.blindsup.notifications.BlindNotification


private const val TAG = "TournamentRunningViewMo"

class TournamentRunningViewModel(private val app: Application) : AndroidViewModel(app) {


    //A value for a minute in time
    private val minute: Long = 60_000L

    //A value for a second in time
    private val second: Long = 1_000L

    // a position value to move through blind levels
    private var position = 0

    //A boolean to handle the pause feature
    private var isPaused = false

    // A boolean to handle the beginning of the tournament
    private var isNew = true

    // a value to track remaining time in the current blind level
    private var millisecondLeft = 0L

    // A variable to hold the users blind length
    var userTimeSelection = 0L

    // A mutable list to hold the tournament structure data
    private var blindsList = mutableListOf<TournamentData>()


   // private val notificationIntent = Intent(app, AlarmReceiver::class.java)
   // private var pendingIntent: PendingIntent = PendingIntent.getBroadcast(
//        getApplication(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT
//    )

    // A global variable for our countdown timer.
    private lateinit var timer: CountDownTimer

    //Initializing LiveData for
    private val _elapsedTime = MutableLiveData<Long>()
    val elapsedTime: LiveData<Long>
        get() = _elapsedTime

    //Initializing LiveData for
    private val _smallBlind = MutableLiveData<Int>()
    val smallBlind: LiveData<Int>
        get() = _smallBlind

    //Initializing LiveData for
    private val _bigBlind = MutableLiveData<Int>()
    val bigBlind: LiveData<Int>
        get() = _bigBlind

    //Initializing LiveData for
    private val _nextSmallBlind = MutableLiveData<Int>()
    val nextSmallBlind: LiveData<Int>
        get() = _nextSmallBlind

    //Initializing LiveData for
    private val _nextBigBlind = MutableLiveData<Int>()
    val nextBigBlind: LiveData<Int>
        get() = _nextBigBlind

    //Initializing LiveData for
    private val _currentLevel = MutableLiveData<Int>()
    val currentLevel: LiveData<Int>
        get() = _currentLevel

    //Initializing LiveData for
    private val _nextLevel = MutableLiveData<Int>()
    val nextLevel: LiveData<Int>
        get() = _nextLevel

    //Initializing LiveData for
    private val _pauseText = MutableLiveData<String>()
    val pauseText: LiveData<String>
        get() = _pauseText

    // Using an initializer block to simply set the button Text to "Start Tournament"
    init {
        _pauseText.value = "Start Tournament"

    }

    // This function handles the clicks on the start/pause/resume button
    fun timerControl() {
        //If this tournament is newly loaded. start the tournament
        if (isNew) {
            isNew = false
            _pauseText.value = "Pause Tournament"
            createTimer()

            //If the tournament is not new, handle pause and resume
        } else {

            // If the tournament is paused, resume the tournament at the correct remaining time
            if (isPaused) {
                isPaused = false
                _pauseText.value = "Pause Tournament"
                //this function loads the remaining time opposed to a new level
                resumeTimer()
            }
            //If the tournament is running, and the button is clicked,
            // pause the tournament
            else {
                isPaused = true
                _pauseText.value = "Resume Tournament"
                timer.cancel()
            }
        }
    }

    // A function to start the timer for a blind level
    private fun createTimer() {

        //Creating a timer variable that uses elapsed time as a metric
        val triggerTime = SystemClock.elapsedRealtime() + userTimeSelection

        //Creating a timer object
        timer = object : CountDownTimer(triggerTime, second) {
            override fun onTick(p0: Long) {
                //Set our liveData to match our countdown timer
                _elapsedTime.value = triggerTime - SystemClock.elapsedRealtime()
                //set our remaining time variable in case the user pauses the tournament
                millisecondLeft = _elapsedTime.value!!

                //Once we hit 0 seconds left, call the resetTimer function
                if (_elapsedTime.value!! <= 0) {
                    resetTimer()
                }
            }

            override fun onFinish() {
                resetTimer()

            }
        }
        // Now that we set up our timer, start the timer.
        timer.start()
    }

    //This function is called to change all of our blinds aand levels variables and reset
    // the timer for the next blind level
    fun resetTimer() {
        //Variable to move level
        position++
        //These variables are the algorithims to increase the blind levels consistently
        // based on the users smallest chip denomination
        _smallBlind.value = _smallBlind.value!! * 2
        _bigBlind.value = _bigBlind.value!! * 2
        _nextSmallBlind.value = _smallBlind.value!! * 2
        _nextBigBlind.value = _bigBlind.value!! * 2
        _currentLevel.value = position + 1
        _nextLevel.value = position + 2
        timer.cancel()
        _elapsedTime.value = 0

        //Trigger a notification to let the user know the blinds are up and display the new
        // blind levels
        BlindNotification().triggerNotification(
            "Blinds are now ${_smallBlind.value}/${_bigBlind.value}",
            app
        )
        // Restart the timer with the updated variables
        createTimer()
    }

    // This function is getting the selected tournament data from Firebase
    fun fetchSavedData(selection: Int, context: Context) {
        val acct = GoogleSignIn.getLastSignedInAccount(context)
        val auth = Firebase.auth
        if (auth.currentUser != null) {
            val databaseBlinds =
                FirebaseDatabase.getInstance().getReference("Users/${acct!!.id}/Blinds")

            databaseBlinds.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (blindsSnapshot in snapshot.children) {
                            val userBlinds = blindsSnapshot.getValue(TournamentData::class.java)
                            userBlinds?.id = blindsSnapshot.key


                            if (userBlinds != null) {

                                blindsList.add(userBlinds)
                                println("added one $userBlinds")
                            }
                        }

                    }
                    // Setting all the initial Livedata variables based on the SavedTournament
                    // The user has selected in the previous Screen
                    userTimeSelection = blindsList[selection].blindLength * minute
                    _elapsedTime.value = userTimeSelection
                    _smallBlind.value = blindsList[selection].smallestChip
                    _bigBlind.value = blindsList[selection].smallestChip * 2
                    _nextSmallBlind.value = blindsList[selection].smallestChip * 2
                    _nextBigBlind.value = blindsList[selection].smallestChip * 4

                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e(TAG, "onCancelled: error")
                }
            })
        }
    }

    // This function is to resume the timer after a pause
    private fun resumeTimer() {
        // Set the timer to our paused time via the millisecondLeft variable.
        val triggerTime = SystemClock.elapsedRealtime() + millisecondLeft

        //The same setup as our createTimer function just with our resume time loaded.
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
    }

    fun endTournament() {
        if (!isNew) {
            timer.cancel()
        }
    }
}

