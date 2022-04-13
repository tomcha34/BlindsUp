package com.mdev.blindsup.ui.tournamentRunning

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.format.DateUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.mdev.blindsup.R
import com.mdev.blindsup.data.Constants
import com.mdev.blindsup.databinding.ActivityTournamentRunningBinding
import com.mdev.blindsup.notifications.BlindNotification
import com.mdev.blindsup.ui.home.HomeActivity

class TournamentRunningActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTournamentRunningBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //initializing the binding utility
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tournament_running)

        //run our function to create a notification channel
        createChannel()

        //initialize our viewModel via ViewModelProvider
        val runningViewModel = ViewModelProvider(this).get(
            TournamentRunningViewModel::class.java
        )

        // Retrieve the position clicked from the SavedTournamentActivity
        val savedTournament = intent.getIntExtra(Constants().SAVED_SELECTION, 0)

        //A function to fetch the tournament data, passing the position clicked,
        // and context as parameters
        runningViewModel.fetchSavedData(savedTournament, this)

        //Observing Livedata ourCountdown timer and showing it in our UI
        runningViewModel.elapsedTime.observe(this, {
            binding.timeLeftTextView.text = it.toString()
            binding.timeLeftTextView.setElapsedTime(it)
        })
        //Observing Livedata for our start/pause/resume button
        // and showing the proper button text in our UI
        runningViewModel.pauseText.observe(this, {
            binding.pauseResumeButton.text = it
        })

        // Observing Livedata for our smallBlind, and showing it in our UI
        runningViewModel.smallBlind.observe(this, { small ->
            // Observing Livedata for our bigBlind, and showing it in our UI
            runningViewModel.bigBlind.observe(this, { big ->
                binding.currentBlindsTextView.text = "Blinds: $small/$big"
            })
        })
        // Observing Livedata for our next level smallBlind, and showing it in our UI
        runningViewModel.nextSmallBlind.observe(this, { nextSmall ->
            // Observing Livedata for our next level smallBlind, and showing it in our UI
            runningViewModel.nextBigBlind.observe(this, { nextBig ->
                binding.nextBlindsTextView.text = "Next Blinds: $nextSmall/$nextBig"
            })
        })

        // Observing Livedata for our current level , and showing it in our UI
        runningViewModel.currentLevel.observe(this, {
            binding.currentLevelTextView.text = "Current Level: $it"
        })
        // Observing Livedata for our next level , and showing it in our UI
        runningViewModel.nextLevel.observe(this, {
            binding.nextLevelTextView.text = "Next Level $it"
        })

        //When the start/pause/resume button is clicked, call the function
        // to handle the requested action
        binding.pauseResumeButton.setOnClickListener {
            runningViewModel.timerControl()
        }

        // a clicklistener to end the tournament
        binding.endTournamentButton.setOnClickListener {
            runningViewModel.endTournament()
            startActivity(Intent(this, HomeActivity::class.java))

        }

    }

    // A function to create a notification channel as it is required
    // in android versions "O" and above
    private fun createChannel() {
        val notificationManager =
            this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mChannel = NotificationChannel(
                Constants().CHANNEL_ID,
                Constants().CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            mChannel.description = "Blinds notification"
            mChannel.enableLights(true)
            mChannel.lightColor = Color.RED
            mChannel.enableVibration(true)
            notificationManager.createNotificationChannel(mChannel)
        }
    }

    //This is an extension function built off of TextView
    // it's purpose is to show full minutes and seconds in the proper format
    // that a user would expect is a countdown timer.
    private fun TextView.setElapsedTime(value: Long) {
        val seconds = value / 1000
        text = if (seconds < 60) seconds.toString() else DateUtils.formatElapsedTime(seconds)
    }


}