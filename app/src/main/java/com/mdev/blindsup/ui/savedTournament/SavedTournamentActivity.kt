package com.mdev.blindsup.ui.savedTournament

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.mdev.blindsup.R
import com.mdev.blindsup.adapter.SavedTournamentAdapter
import com.mdev.blindsup.data.Constants
import com.mdev.blindsup.databinding.ActivitySavedTournamentBinding
import com.mdev.blindsup.ui.newTournament.NewTournamentActivity
import com.mdev.blindsup.ui.tournamentRunning.TournamentRunningActivity

class SavedTournamentActivity : AppCompatActivity(), SavedTournamentAdapter.OnItemClickListener {
    private lateinit var binding: ActivitySavedTournamentBinding

    //Creating a global instance of our custom adapter
    val adapter = SavedTournamentAdapter(this, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //initializing the binding utility
        binding = DataBindingUtil.setContentView(this, R.layout.activity_saved_tournament)

        //Getting an instance of the viewModel via ViewModelProvider
        val savedViewModel = ViewModelProvider(this).get(SavedTournamentViewModel::class.java)

        //setting the adapter to our recyclerView
        binding.recyclerview.adapter = adapter

        //Calling our custom adapters setData function to populate the tournaments
        adapter.setBlindData(savedViewModel.blinds.value!!)
        //The tournament data is livedata in the ViewModel, we will observe it
        //and once it loads, populate it on the screen
        savedViewModel.blinds.observe(this, {
            adapter.setBlindData(it)
        })




        //A button to create a new tournament
        binding.floatingActionButton.setOnClickListener {
        startActivity(Intent(this, NewTournamentActivity::class.java))

        }
    }

    override fun onItemClick(position: Int) {
        //create an intent to navigate to the RunningTournamentActivity
        val intent = Intent(this, TournamentRunningActivity::class.java)
        //Add an intentExtra to pass the position clicked so we know which tournament to load.
        intent.putExtra(Constants().SAVED_SELECTION, position)
        startActivity(intent)
    }
}