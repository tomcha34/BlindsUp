package com.mdev.blindsup.ui.newTournament

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.mdev.blindsup.R
import com.mdev.blindsup.databinding.ActivityNewTournamentBinding
import com.mdev.blindsup.ui.savedTournament.SavedTournamentActivity
import com.mdev.blindsup.ui.tournamentRunning.TournamentRunningActivity

class NewTournamentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewTournamentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //initializing the binding utility
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_tournament)

        //getting an instance of our viewModel via ViewModelProvider
        val newTournamentViewModel =
            ViewModelProvider(this).get(NewTournamentViewModel::class.java)

        //This is a list for editText validation
        val validationList = arrayListOf(
            binding.blindLevelInputLayout,
            binding.nicknameInputLayout,
            binding.startingStackInputLayout,
            binding.smallestChipInputLayout
        )
        //OnClickListener for creating a new tournament
        binding.createTournamentButton.setOnClickListener {

            //If any fields are empty, don't allow a save and let user know.
            if (binding.blindLevelEditText.text.isNullOrEmpty() || binding.nicknameEditText.text.isNullOrEmpty() ||
                binding.startingStackEditText.text.isNullOrEmpty() || binding.smalledChipEditText.text.isNullOrEmpty()
            ) {
                for (fields in validationList) {
                    if (fields.editText?.text.isNullOrEmpty()) {
                        fields.error = "Field must not be empty"
                    } else {
                        fields.error = null
                    }
                }
            } else {


                //If all fields are filled in, create the new tournament
                newTournamentViewModel.createTournament(
                    binding.nicknameEditText.text.toString(),
                    binding.blindLevelEditText.text.toString().toLong(),
                    binding.startingStackEditText.text.toString().toInt(),
                    binding.smalledChipEditText.text.toString().toInt(),
                    this
                )
                //Navigate to the SavedTournamentActivity
                val intent = Intent(this, SavedTournamentActivity::class.java)
                startActivity(intent)
            }
        }


    }
}