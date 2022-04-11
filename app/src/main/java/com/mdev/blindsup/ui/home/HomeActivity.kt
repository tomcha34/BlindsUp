package com.mdev.blindsup.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.snackbar.Snackbar
import com.mdev.blindsup.R
import com.mdev.blindsup.databinding.ActivityHomeBinding
import com.mdev.blindsup.ui.login.LoginActivity
import com.mdev.blindsup.ui.newTournament.NewTournamentActivity
import com.mdev.blindsup.ui.savedTournament.SavedTournamentActivity

class HomeActivity : AppCompatActivity() {
    private lateinit var binding : ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)

        val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        binding.newTourneyButton.setOnClickListener {
            startActivity(Intent(this, NewTournamentActivity::class.java))
        }

        binding.savedTourneyButton.setOnClickListener {
            startActivity(Intent(this, SavedTournamentActivity::class.java))
        }

        binding.logout.setOnClickListener {
            val gso = GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            homeViewModel.logoutUser(this, gso)
            homeViewModel.showSnackbar(this)
            startActivity(Intent(this, LoginActivity::class.java))


        }


    }

    // Blocking access to the Login Screen
    override fun onBackPressed() {
    }
}