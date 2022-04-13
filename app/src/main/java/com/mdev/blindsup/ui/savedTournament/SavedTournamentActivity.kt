package com.mdev.blindsup.ui.savedTournament

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
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
    private val adapter = SavedTournamentAdapter(this, this)
    private lateinit var recyclerView: RecyclerView
    private lateinit var savedViewModel: SavedTournamentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //initializing the binding utility
        binding = DataBindingUtil.setContentView(this, R.layout.activity_saved_tournament)

        //Getting an instance of the viewModel via ViewModelProvider
        savedViewModel = ViewModelProvider(this).get(SavedTournamentViewModel::class.java)

        recyclerView = binding.recyclerview
        //setting the adapter to our recyclerView
        recyclerView.adapter = adapter

        //Calling our custom adapters setData function to populate the tournaments
        adapter.setBlindData(savedViewModel.blinds.value!!)
        //The tournament data is livedata in the ViewModel, we will observe it
        //and once it loads, populate it on the screen
        savedViewModel.blinds.observe(this, {
            adapter.setBlindData(it)
            adapter.notifyDataSetChanged()
        })


        //A button to create a new tournament
        binding.floatingActionButton.setOnClickListener {
            startActivity(Intent(this, NewTournamentActivity::class.java))

        }
    }

    override fun onItemClick(position: Int) {

        //Show an alert dialog when the user selects a saved session.
        //Allow them to either start the selection, or delete it
        val builder: AlertDialog.Builder =
            AlertDialog.Builder(this)
        builder.setTitle("Options")
        builder.setMessage("What would you like to do?")

        //This is the code is the user selected to delete
        builder.setPositiveButton(
            "Delete Tournament"
        ) { dialog, _ ->

            //Run the ViewModel delete function to remove from Firebase
            savedViewModel.deleteTournament(position)
            //Also update our UI
            savedViewModel.blinds.value?.let { adapter.setBlindData(it) }
            //Included a snackbar for additional confirmation
            Snackbar.make(
                binding.floatingActionButton,
                "Session successfully deleted", Snackbar.LENGTH_SHORT
            ).show()
            dialog.dismiss()

        }

        //This will be called when the user wants to start the tournament
        builder.setNegativeButton(
            "Start Tournament"
        ) { _, _ ->
            //create an intent to navigate to the RunningTournamentActivity
            val intent = Intent(this, TournamentRunningActivity::class.java)
            //Add an intentExtra to pass the position clicked so we know which tournament to load.
            intent.putExtra(Constants().SAVED_SELECTION, position)
            startActivity(intent)
        }

        //Initializing and populating the alert
        val alert: AlertDialog = builder.create()
        alert.show()


    }
}