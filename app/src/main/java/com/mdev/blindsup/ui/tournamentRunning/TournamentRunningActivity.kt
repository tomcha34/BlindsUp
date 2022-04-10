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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mdev.blindsup.R
import com.mdev.blindsup.databinding.ActivityTournamentRunningBinding
import com.mdev.blindsup.ui.home.HomeActivity

class TournamentRunningActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTournamentRunningBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tournament_running)
        createChannel()
        val runningViewModel = ViewModelProvider(this).get(
            TournamentRunningViewModel::class.java
        )
       //binding.tournamentRunningViewModel = runningViewModel

        runningViewModel.elapsedTime.observe(this, Observer {

            binding.timeLeftTextView.text = it.toString()
            binding.timeLeftTextView.setElapsedTime(it)

        })

        runningViewModel.smallBlind.observe(this, {
            val small = it
            runningViewModel.bigBlind.observe(this, {
                binding.currentBlindsTextView.text = "Blinds: $small/$it"
            })

        })

        runningViewModel.nextSmallBlind.observe(this, {
            val nextSmall = it
            runningViewModel.nextBigBlind.observe(this, {
                binding.nextBlindsTextView.text = "Blinds: $nextSmall/$it"
            })

        })

        runningViewModel.currentLevel.observe(this, {
            binding.currentLevelTextView.text = "Current Level: $it"
        })

        runningViewModel.nextLevel.observe(this, {
            binding.nextLevelTextView.text = "Next Level $it"
        })



       // binding.currentBlindsTextView.text = "Blinds $small/$big"









//        val triggerTime = SystemClock.elapsedRealtime() + 1_000L * 110
//        // println(time)
//      //  println(_elapsedTime.value)
//       val timer = object : CountDownTimer(triggerTime, 1_000L) {
//            override fun onTick(p0: Long) {
//                val time = triggerTime - SystemClock.elapsedRealtime()
//                val seconds = time / 1000
//                binding.timeLeftTextView.text = runningViewModel.elapsedTime.toString()
//                //  println(elapsedTime)
//                if (time <= 0) {
//                   // resetTimer()
//                }
//            }
//
//            override fun onFinish() {
//              //  resetTimer()
//            }
//        }
//        timer.start()


        binding.pauseResumeButton.setOnClickListener {
            runningViewModel.beginTimer()
//            binding.timeLeftTextView.text = runningViewModel.elapsedTime.toString()
        }
//        val seconds = runningViewModel.elapsedTime.value
//        if (seconds != null){
//            binding.timeLeftTextView.setElapsedTime(seconds!!)
//            println(seconds)
//        }
//        else {
//            binding.timeLeftTextView.setElapsedTime(60000L)
//        }

        binding.endTournamentButton.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))


        }


    }
    fun createChannel() {
        val NOTIFICATION_ID = 234
        val notificationManager =
            this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val CHANNEL_ID: String
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CHANNEL_ID = "my_channel_01"
            val name: CharSequence = "my_channel"
            val Description = "This is my channel"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
            mChannel.description = Description
            mChannel.enableLights(true)
            mChannel.lightColor = Color.RED
            mChannel.enableVibration(true)
            mChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            mChannel.setShowBadge(false)
            notificationManager.createNotificationChannel(mChannel)
        }



    }
    fun TextView.setElapsedTime(value: Long) {
        val seconds = value / 1000
        text = if (seconds < 60) seconds.toString() else DateUtils.formatElapsedTime(seconds)
    }
}