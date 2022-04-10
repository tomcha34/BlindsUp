package com.mdev.blindsup.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.mdev.blindsup.R
import com.mdev.blindsup.databinding.ActivityHomeBinding
import com.mdev.blindsup.ui.newTournament.NewTournamentActivity

class HomeActivity : AppCompatActivity() {
    private lateinit var binding : ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)

        binding.newTourneyButton.setOnClickListener {
            startActivity(Intent(this, NewTournamentActivity::class.java))
        }
    }
}